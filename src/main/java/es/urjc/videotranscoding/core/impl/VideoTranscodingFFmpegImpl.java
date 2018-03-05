package es.urjc.videotranscoding.core.impl;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.urjc.videotranscoding.core.VideoTranscodingService;
import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.exception.FFmpegException;
import es.urjc.videotranscoding.exception.FFmpegRuntimeException;
import es.urjc.videotranscoding.service.ConversionService;
import es.urjc.videotranscoding.service.impl.FileUtilsFFmpegImpl;
import es.urjc.videotranscoding.wrapper.FfmpegResourceBundle;

@Service
public class VideoTranscodingFFmpegImpl implements VideoTranscodingService {
	/**
	 * TRACE and log4J
	 */
	private static final Logger logger = Logger.getLogger(VideoTranscodingFFmpegImpl.class);
	private static final String FICH_TRAZAS = "fichero.mensajes.trazas";
	private static final String TRACE_FFMPEG_NULL_OR_EMPTY = "ffmpeg.nullOrEmpty";
	private static final String TRACE_FFMPEG_NOT_FOUND = "ffmpeg.notFound";
	private static final String TRACE_FOLDER_OUTPUT_NULL_OR_EMPTY = "ffmpeg.folderOuput.nullOrEmpty";
	private static final String TRACE_FOLDER_OUPUT_NOT_EXISTS = "ffmpeg.folderOutput.notExits";
	private static final String TRACE_ORIGINAL_VIDEO_NULL = "ffmpeg.originalVideo.null";
	private static final String TRACE_ORIGINAL_VIDEO_NOT_IS_SAVE = "ffmpeg.originalVideo.notSave";
	private static final String TRACE_INTERRUP_EXCEPTION = "ffmpeg.interrupt.exception";
	private static final String TRACE_IO_EXCEPTION_BY_EXEC = "ffmpeg.io.exception.exec";
	private static final String TRACE_EXCEPTION_EXECUTOR_SERVICE = "ffmpeg.exception.executor.service";
	/**
	 * Paths Instalation FFMPEG
	 */
	private final String FFMPEG_INSTALLATION_CENTOS7 = "path.ffmpeg.centos";
	private final String FFMPEG_INSTALLATION_MACOSX = "path.ffmpeg.macosx";
	private final String DEFAULT_UPLOAD_FILES = "path.folder.ouput";
	/**
	 * 
	 */
	private StreamGobbler errorGobbler;
	private StreamGobbler inputGobbler;
	private StreamGobbler outputGobbler;
	private final ExecutorService executorService = Executors.newFixedThreadPool(3);
	static ExecutorService serviceConversion = Executors.newFixedThreadPool(1);

	@Resource
	private FfmpegResourceBundle ffmpegResourceBundle;
	@Resource
	private Properties propertiesFFmpeg;
	@Resource
	private Properties propertiesFicheroCore;

	@Autowired
	private FileUtilsFFmpegImpl fileUtilsService;
	@Autowired
	private ConversionService conversionService;
	@Autowired
	private StreamGobblerFactory streamGobblerPersistentFactory;
	// @Autowired
	// private OriginalVideoService originalVideoService;
	// TODO JAVADOC, LOGGER, EXCEPTS

	@PostConstruct
	public void init() {
		// propertiesFicheroCore = (Properties)
		// ApplicationContextProvider.getApplicationContext()
		// .getBean("propertiesFicheroCore");
		logger.setResourceBundle(ffmpegResourceBundle
				.getFjResourceBundle(propertiesFicheroCore.getProperty(FICH_TRAZAS), Locale.getDefault()));
	}

	private String getPathOfProgram() throws FFmpegException {
		// TODO otros casos
		String pathFFMPEG;
		if ((System.getProperty("os.name").equals("Mac OS X"))) {
			pathFFMPEG = propertiesFFmpeg.getProperty(FFMPEG_INSTALLATION_MACOSX);
		} else {
			pathFFMPEG = propertiesFFmpeg.getProperty(FFMPEG_INSTALLATION_CENTOS7);
		}
		if (StringUtils.isBlank(pathFFMPEG)) {
			logger.l7dlog(Level.ERROR, TRACE_FFMPEG_NULL_OR_EMPTY, null);
			throw new FFmpegException(FFmpegException.EX_FFMPEG_EMPTY_OR_NULL);
		}
		if (!fileUtilsService.exitsFile(pathFFMPEG)) {
			logger.l7dlog(Level.ERROR, TRACE_FFMPEG_NOT_FOUND, new String[] { pathFFMPEG }, null);
			throw new FFmpegException(FFmpegException.EX_FFMPEG_NOT_FOUND, new String[] { pathFFMPEG });
		}
		return pathFFMPEG;
	}

	private String getPathToSaveFiles() throws FFmpegException {
		String folderOutput = propertiesFFmpeg.getProperty(DEFAULT_UPLOAD_FILES);
		if (StringUtils.isBlank(folderOutput)) {
			logger.l7dlog(Level.ERROR, TRACE_FOLDER_OUTPUT_NULL_OR_EMPTY, null);
			throw new FFmpegException(FFmpegException.EX_FOLDER_OUTPUT_EMPTY_OR_NULL);
		}
		if (!fileUtilsService.exitsPath(folderOutput)) {
			logger.l7dlog(Level.ERROR, TRACE_FOLDER_OUPUT_NOT_EXISTS, new String[] { folderOutput }, null);
			throw new FFmpegException(FFmpegException.EX_FOLDER_OUTPUT_NOT_EXITS,
					new String[] { folderOutput });
		}
		return folderOutput;
	}

	/**
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @see
	 */
	public void transcodeVideo(Original original) throws FFmpegException {
		// if (originalVideo!=null)throw new
		// FFmpegException(FFmpegException.EX_FOLDER_OUTPUT_EMPTY_OR_NULL);
		if (original == null) {
			logger.l7dlog(Level.ERROR, TRACE_ORIGINAL_VIDEO_NULL, null);
			throw new FFmpegException(FFmpegException.EX_ORIGINAL_VIDEO_NULL);
		}
		if (!fileUtilsService.exitsFile(original.getPath())) {
			logger.l7dlog(Level.ERROR, TRACE_ORIGINAL_VIDEO_NOT_IS_SAVE, new String[] { original.getPath() },
					null);
			throw new FFmpegException(FFmpegException.EX_ORIGINAL_VIDEO_NOT_IS_SAVE,
					new String[] { original.getPath() });
		}
		String pathFFmpeg = getPathOfProgram();
		File fileOV = new File(original.getPath());
		String pathSaveConvertedVideos = getPathToSaveFiles();
		serviceConversion.execute(new Runnable() {
			public void run() {
				original.getAllConversions().forEach((originalV -> {
					if (!originalV.isActive()) {
						String command = getCommand(pathFFmpeg, fileOV, pathSaveConvertedVideos, originalV);
						try {
							conversionFinal(command, originalV);
						} catch (FFmpegRuntimeException e) {
							logger.l7dlog(Level.ERROR, TRACE_EXCEPTION_EXECUTOR_SERVICE, e);
							throw new FFmpegRuntimeException(e.getCodigo());
						}
					}
				}));
			}
		});
	}

	/**
	 * 
	 * @param command
	 * @param video
	 * @throws FFmpegException
	 */
	private void conversionFinal(String command, Conversion video) throws FFmpegRuntimeException {
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command);
			video.setActive(true);
			conversionService.save(video);
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
		} catch (InterruptedException e) {
			video.setFinished(false);
			video.setActive(false);
			logger.l7dlog(Level.ERROR, TRACE_INTERRUP_EXCEPTION, null);
			throw new FFmpegRuntimeException(FFmpegRuntimeException.EX_EXECUTION_EXCEPTION, e);
		} catch (IOException e) {
			FFmpegRuntimeException ex = new FFmpegRuntimeException(FFmpegRuntimeException.EX_IO_EXCEPTION_BY_EXEC,
					new String[] { command }, e);
			logger.l7dlog(Level.ERROR, TRACE_IO_EXCEPTION_BY_EXEC, new String[] { command }, ex);
			video.setFinished(false);
			video.setActive(false);
			throw ex;
		} finally {
			conversionService.save(video);
		}
	}

	/**
	 * 
	 * @param pathFFMPEG
	 * @param fileInput
	 * @param folderOutput
	 * @param conversion
	 * @return
	 */
	private String getCommand(String pathFFMPEG, File fileInput, String folderOutput, Conversion conversion) {
		String finalPath = folderOutput
				+ getFinalNameFile(fileInput, conversion.getConversionType().getContainerType());
		conversion.setPath(finalPath);
		conversionService.save(conversion);
		String command = pathFFMPEG + " -i " + fileInput.toString()
				+ conversion.getConversionType().getCodecAudioType()
				+ conversion.getConversionType().getCodecVideoType() + finalPath;
		logger.l7dlog(Level.DEBUG, "El Comando que se va a enviar es :" + command, null);
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
		return FilenameUtils.getBaseName(fileInput.getName()) + sort.substring(3, 9) + extension;
	}

	/**
	 * 
	 */
	public StreamGobbler getErrorGobbler() {
		return errorGobbler;
	}

	/**
	 * 
	 */
	public StreamGobbler getInputGobbler() {
		return inputGobbler;
	}

	/**
	 * 
	 */
	public StreamGobbler getOutputGobbler() {
		return outputGobbler;
	}

}
