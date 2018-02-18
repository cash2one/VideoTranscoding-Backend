package urjc.videotranscoding.core.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FilenameUtils;
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
import urjc.videotranscoding.service.OriginalVideoService;
import urjc.videotranscoding.wrapper.FfmpegResourceBundle;

@Service
public class VideoTranscodingFFmpegImpl implements VideoTranscodingService {
	private static final String FICH_TRAZAS = "fichero.mensajes.trazas";

	// private static final Logger log =
	// Logger.getLogger(DocumentosServiceImpl.class);

	// private static final Logger logger =
	// LogManager.getLogger(FFmpegTranscondingPersistentImpl.class);
	private static final Logger logger = Logger.getLogger(VideoTranscodingFFmpegImpl.class);

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
//		propertiesFicheroCore = (Properties) ApplicationContextProvider.getApplicationContext()
//				.getBean("propertiesFicheroCore");
		logger.setResourceBundle(ffmpegResourceBundle
				.getFjResourceBundle(propertiesFicheroCore.getProperty(FICH_TRAZAS), Locale.getDefault()));
	}

	@Override
	public void transcode(File pathFFMPEG, Path folderOutput, OriginalVideo originalVideo) throws FFmpegException {
		if (pathFFMPEG == null || !pathFFMPEG.exists()) {

			FFmpegException x = new FFmpegException(FFmpegException.EX_CONVERSION_TYPE_EMPT);
			logger.l7dlog(Level.ERROR, "cache.error", x);
			throw x;
		}
		if (originalVideo == null || !new File(originalVideo.getOriginalVideo()).exists()) {
			FFmpegException ex = new FFmpegException(FFmpegException.EX_FILE_INPUT_NOT_VALID);

			logger.error("", ex);
			throw ex;
		}
		if (folderOutput == null) {
			FFmpegException ex = new FFmpegException(FFmpegException.EX_FOLDER_OUTPUT_NULL);
			logger.error("", ex);
			throw ex;
		}
		if (!Files.exists(folderOutput)) {
			FFmpegException ex = new FFmpegException(FFmpegException.EX_FOLDER_OUTPUT_NOT_FOUND);
			logger.error("", ex);
			throw ex;
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
	private String getCommand(File pathFFMPEG, File fileInput, Path folderOutput, ConversionType conversionType) {
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
