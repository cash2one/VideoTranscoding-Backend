package urjc.videotranscoding.ffmpeg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import urjc.videotranscoding.codecs.ConversionType;
import urjc.videotranscoding.exception.FFmpegException;

/**
 * @author luisca
 */
@Service
public class FFmpegTranscodingImpl implements TranscodingService{
	private static final Logger logger = LogManager.getLogger(FFmpegTranscodingImpl.class);
	private final String TRACE_CONVERSION_TYPE_NOT_FOUND = "Conversion Type: ";
	private StreamGobbler errorGobbler;
	private StreamGobbler inputGobbler;
	private StreamGobbler outputGobbler;
	@Resource
	private Properties propertiesFFmpeg;
	//TODO JAVADOC, LOGGER

	/**
	 * 
	 */
	public Map<ConversionType,Boolean> transcode(File ffmpegPath,File fileInput,Path folderOutput,
				List<ConversionType> conversionType) throws FFmpegException{
		if(ffmpegPath == null || !ffmpegPath.exists()){
			FFmpegException ex = new FFmpegException(FFmpegException.EX_FFMPEG_NOT_FOUND);
			// TODO
			logger.error("",ex);
			throw ex;
		}
		if(fileInput == null || !fileInput.exists() || fileInput.isDirectory()){
			FFmpegException ex = new FFmpegException(FFmpegException.EX_FILE_INPUT_NOT_VALID);
			logger.error("",ex);
			throw ex;
		}
		if(folderOutput == null){
			FFmpegException ex = new FFmpegException(FFmpegException.EX_FOLDER_OUTPUT_NULL);
			logger.error("",ex);
			throw ex;
		}
		if(!Files.exists(folderOutput)){
			FFmpegException ex = new FFmpegException(FFmpegException.EX_FOLDER_OUTPUT_NOT_FOUND);
			logger.error("",ex);
			throw ex;
		}
		if(conversionType == null){
			FFmpegException ex = new FFmpegException(FFmpegException.EX_NO_CONVERSION_TYPE_FOUND);
		//	logger.error("",new String[]{},null);
			throw ex;
		}
		if(conversionType.isEmpty()){
			FFmpegException ex = new FFmpegException(FFmpegException.EX_CONVERSION_TYPE_EMPTY);
		//TODO	logger.error("",new String[]{TRACE_CONVERSION_TYPE_NOT_FOUND + conversionType.size()});
			throw ex;
		}
		return transcodeFinalVersion(ffmpegPath,fileInput,folderOutput,conversionType);
	}

	/**
	 * @param ffmpegPath
	 * @param fileInput
	 * @param folderOutput
	 * @param conversionType
	 * @return Map with all Conversiontype finished
	 * @throws FFmpegException
	 */
	private Map<ConversionType,Boolean> transcodeFinalVersion(File ffmpegPath,File fileInput,Path folderOutput,
				List<ConversionType> conversionType) throws FFmpegException{
		Map<ConversionType,Boolean> conversionFinished = new HashMap<>();
		String commandF;
		for(ConversionType typeConversion:conversionType){
			try{
				commandF = getCommand(ffmpegPath,fileInput,folderOutput,typeConversion);
				Runtime rt = Runtime.getRuntime();
				Process proc = rt.exec(commandF);
				errorGobbler = new StreamGobbler(proc.getErrorStream(),"ERROR");
				inputGobbler = new StreamGobbler(proc.getInputStream(),"INPUT");
				outputGobbler = new StreamGobbler(proc.getInputStream(),"OUTPUT");
				inputGobbler.start();
				errorGobbler.start();
				outputGobbler.start();
				int exitVal = proc.waitFor();
				if(exitVal == 0)
					conversionFinished.put(typeConversion,true);
				else
					conversionFinished.put(typeConversion,false);
			}catch(ExecuteException e){
				conversionFinished.put(typeConversion,false);
				throw new FFmpegException(e);
			}catch(InterruptedException e){
				conversionFinished.put(typeConversion,false);
				throw new FFmpegException(e);
			}catch(IOException e){
				conversionFinished.put(typeConversion,false);
				throw new FFmpegException(e);
			}
		}
		return conversionFinished;
	}

	/**
	 * @param pathFFMPEG
	 * @param fileInput
	 * @param folderOutput
	 * @param conversionType
	 * @return String with the Command ready for send it.
	 */
	private String getCommand(File pathFFMPEG,File fileInput,Path folderOutput,ConversionType conversionType){
		String command = pathFFMPEG + " -i " + fileInput.toString() + conversionType.getCodecAudioType()
					+ conversionType.getCodecVideoType() + folderOutput
					+ getFinalNameFile(fileInput,conversionType.getContainerType());
		logger.debug("El Comando que se va a enviar es :" + command);
		return command;
	}

	/**
	 * @param fileInput of file to converted.
	 * @param extension of the futher nameFile
	 * @return String with the final name of the file
	 */
	private String getFinalNameFile(File fileInput,String extension){
		String sort = String.valueOf(System.currentTimeMillis());
		return "/" + FilenameUtils.getBaseName(fileInput.getName()) + sort.substring(3,9) + extension;
	}

	public StreamGobbler getErrorGobbler(){
		return errorGobbler;
	}

	public StreamGobbler getInputGobbler(){
		return inputGobbler;
	}

	public StreamGobbler getOutputGobbler(){
		return outputGobbler;
	}
}
