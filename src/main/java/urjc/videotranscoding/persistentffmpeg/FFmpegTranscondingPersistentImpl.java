package urjc.videotranscoding.persistentffmpeg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import urjc.videotranscoding.codecs.ConversionType;
import urjc.videotranscoding.entities.ConversionVideo;
import urjc.videotranscoding.entities.OriginalVideo;
import urjc.videotranscoding.exception.FFmpegException;
import urjc.videotranscoding.ffmpeg.StreamGobbler;
import urjc.videotranscoding.service.ConversionVideoService;
import urjc.videotranscoding.service.OriginalVideoService;

@Service
public class FFmpegTranscondingPersistentImpl implements TranscodingServicePersistent {
	private static final Logger logger = LogManager.getLogger(FFmpegTranscondingPersistentImpl.class);
	private final String TRACE_CONVERSION_TYPE_NOT_FOUND = "Conversion Type: ";
	private StreamGobbler errorGobbler;
	private StreamGobbler inputGobbler;
	private StreamGobbler outputGobbler;
	@Resource
	private Properties propertiesFFmpeg;
	// TODO JAVADOC, LOGGER
	@Autowired
	private OriginalVideoService originalVideoService;
	@Autowired
	private ConversionVideoService conversionVideoService;

	@Override
	public void transcode(File pathFFMPEG, File fileInput, Path folderOutput, OriginalVideo originalVideo)
			throws FFmpegException {
		if (pathFFMPEG == null || !pathFFMPEG.exists()) {
			FFmpegException ex = new FFmpegException(FFmpegException.EX_FFMPEG_NOT_FOUND);
			// TODO
			logger.error("", ex);
			throw ex;
		}
		if (fileInput == null || !fileInput.exists() || fileInput.isDirectory()) {
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

		Thread conversion = new Thread() {
			public void run() {
				for (ConversionVideo video : originalVideo.getAllConversions()) {
					try {
						conversionFinal(getCommand(pathFFMPEG, new File(video.getTitle()), folderOutput,
								video.getConversionType()), video);
					} catch (FFmpegException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				originalVideoService.save(originalVideo);
			}
		};
		conversion.start();
	}

	protected void conversionFinal(String command, ConversionVideo video) throws FFmpegException {
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command);
			errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
			inputGobbler = new StreamGobbler(proc.getInputStream(), "INPUT");
			outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
			inputGobbler.start();
			errorGobbler.start();
			outputGobbler.start();
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

	public StreamGobbler getErrorGobbler() {
		return errorGobbler;
	}

	public StreamGobbler getInputGobbler() {
		return inputGobbler;
	}

	public StreamGobbler getOutputGobbler() {
		return outputGobbler;
	}

}
