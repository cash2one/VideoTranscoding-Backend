package urjc.videotranscoding.core.impl;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import urjc.videotranscoding.codecs.ConversionType;
import urjc.videotranscoding.core.VideoTranscodingService;
import urjc.videotranscoding.entities.ConversionVideo;
import urjc.videotranscoding.entities.OriginalVideo;
import urjc.videotranscoding.exception.FFmpegException;
import urjc.videotranscoding.service.ConversionVideoService;
import urjc.videotranscoding.service.FileUtils;
import urjc.videotranscoding.service.OriginalVideoService;
import urjc.videotranscoding.wrapper.FfmpegResourceBundle;

@Service
public class VideoTranscodingFFmpegImpl implements VideoTranscodingService {
	private static final Logger logger = Logger.getLogger(VideoTranscodingFFmpegImpl.class);
	private static final String FICH_TRAZAS = "fichero.mensajes.trazas";
	private static final String TRACE_FFMPEG_NULL_OR_EMPTY="ffmpeg.nullOrEmpty";
	private static final String TRACE_FFMPEG_NOT_FOUND="ffmpeg.notFound";
	private static final String TRACE_FOLDER_OUTPUT_NULL_OR_EMPTY="ffmpeg.folderOuput.nullOrEmpty";
	private static final String TRACE_FOLDER_OUPUT_NOT_EXISTS="ffmpeg.folderOutput.notExits";
	private static final String TRACE_ORIGINAL_VIDEO_NULL="ffmpeg.originalVideo.null";
	private static final String TRACE_ORIGINAL_VIDEO_NOT_IS_SAVE="ffmpeg.originalVideo.notSave";

	private StreamGobblerPersistent errorGobbler;
	private StreamGobblerPersistent inputGobbler;
	private StreamGobblerPersistent outputGobbler;
	ExecutorService executorService = Executors.newFixedThreadPool(6);
	@Resource
	private FfmpegResourceBundle ffmpegResourceBundle;
	@Resource
	private Properties propertiesFFmpeg;
	@Resource
	private Properties propertiesFicheroCore;

	@Autowired
	private FileUtils fileUtils;
	@Autowired
	private ConversionVideoService conversionVideoService;
	@Autowired
	private OriginalVideoService originalVideoService;
	@Autowired
	private StreamGobblerPersistentFactory streamGobblerPersistentFactory;
	// TODO JAVADOC, LOGGER, EXCEPTS
	// @Autowired
	// private OriginalVideoService originalVideoService;

	@PostConstruct
	public void init() {
		// propertiesFicheroCore = (Properties)
		// ApplicationContextProvider.getApplicationContext()
		// .getBean("propertiesFicheroCore");
		logger.setResourceBundle(ffmpegResourceBundle
				.getFjResourceBundle(propertiesFicheroCore.getProperty(FICH_TRAZAS), Locale.getDefault()));
	}

	/**
	 * 
	 */
	public void transcodeVideo(String pathFFMPEG, String folderOutput, OriginalVideo originalVideo)
			throws FFmpegException {
		if (StringUtils.isBlank(pathFFMPEG)) {
			logger.l7dlog(Level.ERROR, TRACE_FFMPEG_NULL_OR_EMPTY, null);
			throw new FFmpegException(FFmpegException.EX_FFMPEG_EMPTY_OR_NULL);
		}
		
		if (!fileUtils.exitsFile(pathFFMPEG)) {
			logger.l7dlog(Level.ERROR, TRACE_FFMPEG_NOT_FOUND,new String[] {pathFFMPEG} ,null);
			throw new FFmpegException(FFmpegException.EX_FFMPEG_NOT_FOUND,new String [] {pathFFMPEG});

		}
		if (StringUtils.isBlank(folderOutput)) {
			logger.l7dlog(Level.ERROR, TRACE_FOLDER_OUTPUT_NULL_OR_EMPTY, null);
			throw new FFmpegException(FFmpegException.EX_FOLDER_OUTPUT_EMPTY_OR_NULL);
		}

		if (!fileUtils.exitsPath(folderOutput)) {
			logger.l7dlog(Level.ERROR, TRACE_FOLDER_OUPUT_NOT_EXISTS,new String []{folderOutput}, null);
			throw new FFmpegException(FFmpegException.EX_FOLDER_OUTPUT_NOT_EXITS,new String[] {folderOutput});
		}
			
		if (originalVideo == null ) {
			logger.l7dlog(Level.ERROR, TRACE_ORIGINAL_VIDEO_NULL, null);
			throw new FFmpegException(FFmpegException.EX_ORIGINAL_VIDEO_NULL);
		}
		if (!fileUtils.exitsFile(originalVideo.getOriginalVideo())) {
			logger.l7dlog(Level.ERROR, TRACE_ORIGINAL_VIDEO_NOT_IS_SAVE,new String[]{originalVideo.getOriginalVideo()}, null);
			throw new FFmpegException(FFmpegException.EX_ORIGINAL_VIDEO_NOT_IS_SAVE,new String[] {originalVideo.getOriginalVideo()});
		}
		
		
		// if(conversionType == null){
		// FFmpegException ex = new
		// FFmpegException(FFmpegException.EX_NO_CONVERSION_TYPE_FOUND);
		// logger.error("",new String[]{},ex);
		// throw ex;
		// }
		// if(conversionType.isEmpty()){
		// FFmpegException ex = new
		// FFmpegException(FFmpegException.EX_CONVERSION_TYPE_EMPTY);
		// logger.error("",new String[]{TRACE_CONVERSION_TYPE_NOT_FOUND +
		// conversionType.size()},ex);
		// throw ex;
		// }

		Thread conversion = new Thread(new Runnable() {
			@Override
			public void run() {
				for (ConversionVideo video : originalVideo.getAllConversions()) {
					if (!video.isActive()) {
						try {
							System.out.println("Se va a realizar una conversion");
							String command = getCommand(pathFFMPEG, new File(originalVideo.getOriginalVideo()),
									folderOutput, video.getConversionType());
							video.setActive(true);
							originalVideo.setActive(true);
							originalVideoService.save(originalVideo);
							System.out.println("Guardando el video ");
							conversionVideoService.save(video);
							conversionFinal(command, video);

						} catch (FFmpegException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				// originalVideoService.save(originalVideo);
			}
		});
		conversion.start();
		System.out.println("Saliendo del trhread");
	}

	protected void conversionFinal(String command, ConversionVideo video) throws FFmpegException {
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command);
			errorGobbler = streamGobblerPersistentFactory.getStreamGobblerPersistent(proc.getErrorStream(), "ERROR",
					video);
			inputGobbler = streamGobblerPersistentFactory.getStreamGobblerPersistent(proc.getInputStream(), "INPUT",
					video);
			outputGobbler = streamGobblerPersistentFactory.getStreamGobblerPersistent(proc.getInputStream(), "OUTPUT",
					video);
			executorService.execute(errorGobbler);
			executorService.execute(inputGobbler);
			executorService.execute(outputGobbler);
			int exitVal = proc.waitFor();
			if (exitVal == 0) {
				video.setFinished(true);
			} else {
				video.setFinished(false);
			}
		} catch (ExecuteException e) {
			// TODO LOGS
			video.setFinished(false);
			throw new FFmpegException(e);
		} catch (InterruptedException e) {
			// TODO
			video.setFinished(false);
			throw new FFmpegException(e);
		} catch (IOException e) {
			// TODO
			video.setFinished(false);
			throw new FFmpegException(e);
		} finally {
			conversionVideoService.save(video);

		}

	}

	/**
	 * @param pathFFMPEG
	 * @param fileInput
	 * @param folderOutput
	 * @param conversionType
	 * @return String with the Command ready for send it.
	 */
	private String getCommand(String pathFFMPEG, File fileInput, String folderOutput, ConversionType conversionType) {
		String command = pathFFMPEG + " -i " + fileInput.toString() + conversionType.getCodecAudioType()
				+ conversionType.getCodecVideoType() + folderOutput
				+ getFinalNameFile(fileInput, conversionType.getContainerType());
		logger.debug("El Comando que se va a enviar es :" + command);
		return command;
	}

	/**
	 * @param fileInput
	 *            of file to converted.
	 * @param extension
	 *            of the futher nameFile
	 * @return String with the final name of the file
	 */
	private String getFinalNameFile(File fileInput, String extension) {
		String sort = String.valueOf(System.currentTimeMillis());
		return "/" + FilenameUtils.getBaseName(fileInput.getName()) + sort.substring(3, 9) + extension;
	}

	public StreamGobblerPersistent getErrorGobbler() {
		return errorGobbler;
	}

	public StreamGobblerPersistent getInputGobbler() {
		return inputGobbler;
	}

	public StreamGobblerPersistent getOutputGobbler() {
		return outputGobbler;
	}

}
