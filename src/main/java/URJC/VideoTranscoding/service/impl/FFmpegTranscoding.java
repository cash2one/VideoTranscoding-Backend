package URJC.VideoTranscoding.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import URJC.VideoTranscoding.codecs.ConversionType;
import URJC.VideoTranscoding.exception.FFmpegException;
import URJC.VideoTranscoding.service.StreamGobbler;
import URJC.VideoTranscoding.service.TranscodingService;
import URJC.VideoTranscoding.wrapper.FfmpegResourceBundleMessageSource;

/**
 * @author luisca
 */
@Service
class FFmpegTranscoding implements TranscodingService {
	private static final Logger logger = Logger.getLogger(FFmpegTranscoding.class);
	private static final String FICH_TRAZAS = "fichero.mensajes.trazas";
	private static final String TRACE_CONVERSION_TYPE_NOT_FOUND = "Conversion Type: ";
	@Resource
	Properties propertiesFFmpeg;
	@Autowired
	private FfmpegResourceBundleMessageSource trazasMessageSourceFFmpeg;

	@PostConstruct
	void init() {
		logger.setResourceBundle(trazasMessageSourceFFmpeg
				.getResourceBundleLog4j(propertiesFFmpeg.getProperty(FICH_TRAZAS), Locale.getDefault()));
	}

	/**
	 * @param ffmpegPath
	 * @param fileInput
	 * @param folderOutput
	 * @param conversionType
	 * @return
	 * @throws FFmpegException
	 */
	@Override
	public Map<ConversionType, Boolean> transcode(File ffmpegPath, File fileInput, Path folderOutput,
			List<ConversionType> conversionType) throws FFmpegException {
		if (ffmpegPath == null || !ffmpegPath.exists()) {
			FFmpegException ex = new FFmpegException(FFmpegException.EX_FFMPEG_NOT_FOUND);
			logger.l7dlog(Level.ERROR, "", ex);
			throw ex;
		}
		if (fileInput == null || !fileInput.exists() || fileInput.isDirectory()) {
			FFmpegException ex = new FFmpegException(FFmpegException.EX_FILE_INPUT_NOT_VALID);
			logger.l7dlog(Level.ERROR, "", ex);
			throw ex;
		}
		if (folderOutput == null) {
			FFmpegException ex = new FFmpegException(FFmpegException.EX_FOLDER_OUTPUT_NULL);
			logger.l7dlog(Level.ERROR, "", ex);
			throw ex;
		}
		if (!Files.exists(folderOutput)) {
			FFmpegException ex = new FFmpegException(FFmpegException.EX_FOLDER_OUTPUT_NOT_FOUND);
			logger.l7dlog(Level.ERROR, "", ex);
			throw ex;
		}
		if (conversionType == null) {
			FFmpegException ex = new FFmpegException(FFmpegException.EX_NO_CONVERSION_TYPE_FOUND);
			logger.l7dlog(Level.ERROR, "", new String[] {}, ex);
			throw ex;
		}
		if (conversionType.isEmpty()) {
			FFmpegException ex = new FFmpegException(FFmpegException.EX_CONVERSION_TYPE_EMPTY);
			logger.l7dlog(Level.ERROR, "", new String[] { TRACE_CONVERSION_TYPE_NOT_FOUND + conversionType.size() },
					ex);
			throw ex;
		}
		for (ConversionType iterator : conversionType) {
			System.out.println(iterator);
		}
		return transcodeFinalVersion(ffmpegPath, fileInput, folderOutput, conversionType);
	}

	/**
	 * @param ffmpegPath
	 * @param fileInput
	 * @param folderOutput
	 * @param conversionType
	 * @return
	 */
	private Map<ConversionType, Boolean> transcodeFinalVersion(File ffmpegPath, File fileInput, Path folderOutput,
			List<ConversionType> conversionType) {
		Map<ConversionType, Boolean> conversionFinished = new HashMap<>();
		String commandF;
		for (ConversionType typeConversion : conversionType) {
			try {
				commandF = getCommand(ffmpegPath, fileInput, folderOutput, typeConversion);
				System.out.println(commandF);
				Runtime rt = Runtime.getRuntime();
				Process proc = rt.exec(commandF);// AQUI PUEDE TIRAR IO EXCEPTION
				StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
				StreamGobbler inputGobbler = new StreamGobbler(proc.getInputStream(), "INPUT");
				// TODO outputStream
				StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
				inputGobbler.start();
				errorGobbler.start();
				outputGobbler.start();
				int exitVal = proc.waitFor();
				if (exitVal == 0)
					conversionFinished.put(typeConversion, true);
				else
					conversionFinished.put(typeConversion, false);
				System.out.println("ExitValue: " + exitVal);
				return conversionFinished;
			} catch (ExecuteException e) {
				conversionFinished.put(typeConversion, false);
				// TODO Exception
				e.printStackTrace();
				return conversionFinished;
			} catch (InterruptedException e) {
				conversionFinished.put(typeConversion, false);
				// TODO Exception
				e.printStackTrace();
				return conversionFinished;
			} catch (IOException e) {
				conversionFinished.put(typeConversion, false);
				e.printStackTrace();
				return conversionFinished;
			}
		}
		return conversionFinished;
	}

	/**
	 * @param pathFFMPEG
	 * @param fileInput
	 * @param folderOutput
	 * @param conversionType
	 * @return
	 */
	private String getCommand(File pathFFMPEG, File fileInput, Path folderOutput, ConversionType conversionType) {
		return pathFFMPEG + " -i " + fileInput.toString() + conversionType.getCodecAudioType()
				+ conversionType.getCodecVideoType() + folderOutput
				+ getFinalNameFile(fileInput, conversionType.getContainerType());
	}

	/**
	 * @param fileInput
	 * @param extension
	 * @return
	 */
	private String getFinalNameFile(File fileInput, String extension) {
		String sort = String.valueOf(System.currentTimeMillis());
		return "/" + FilenameUtils.getBaseName(fileInput.getName()) + sort.substring(3, 9) + extension;
	}

	/**
	 * @param pathFFMPEG
	 * @param fileInput
	 * @param folderOutput
	 * @param conversionType
	 */
	@SuppressWarnings("unused")
	private void Trans(String pathFFMPEG, File fileInput, Path folderOutput, List<Integer> conversionType) {
		try {
			String sort = String.valueOf(System.currentTimeMillis());
			String[] commmand = new String[] {};
			// = new String[] { pathFFMPEG, " -i ", fileInput.toString(),
			// " -c:a " + AudioCodec.LIBVORBIS, " -c:v " + VideoCodec.VP9, " " +
			// folderOutput + "/"
			// + FilenameUtils.getBaseName(fileInput.getName()) + sort.substring(4, 8) +
			// ".webm" };
			for (String string : commmand) {
				System.out.print(string);
			}
			ProcessBuilder p = new ProcessBuilder(commmand);
			p.redirectErrorStream(true);
			Process process = p.start();
			InputStream out = process.getInputStream();
			InputStream error = process.getErrorStream();
			OutputStream in = process.getOutputStream();
			byte[] buffer = new byte[4000];
			while (isAlive(process)) {
				int no = out.available();
				if (no > 0) {
					int n = out.read(buffer, 0, Math.min(no, buffer.length));
					System.out.println(new String(buffer, 0, n));
				}
				int noe = error.available();
				if (noe > 0) {
					int n = error.read(buffer, 0, Math.min(noe, buffer.length));
					System.out.println(new String(buffer, 0, n));
				}
				int ni = System.in.available();
				if (ni > 0) {
					int n = System.in.read(buffer, 0, Math.min(ni, buffer.length));
					in.write(buffer, 0, n);
					in.flush();
				}
				Thread.sleep(1000);
			}
			System.out.println(process.exitValue());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private boolean isAlive(Process p) {
		try {
			p.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}

	/**
	 * @param pathFFMPEG
	 * @param fileInput
	 * @param folderOutput
	 * @param conversionType
	 */
	@SuppressWarnings("unused")
	private void Trans2(String pathFFMPEG, File fileInput, Path folderOutput, List<ConversionType> conversionType) {
		try {
			String commandF = "";
			// = pathFFMPEG + fileInput.toString() + AudioCodec.LIBVORBIS + VideoCodec.VP9 +
			// folderOutput
			// + getFinalNameFile(fileInput);
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(commandF);
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
			StreamGobbler inputGobbler = new StreamGobbler(proc.getInputStream(), "INPUT");
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
			inputGobbler.start();
			errorGobbler.start();
			outputGobbler.start();
			int exitVal;
			exitVal = proc.waitFor();
			System.out.println("ExitValue: " + exitVal);
		} catch (ExecuteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
