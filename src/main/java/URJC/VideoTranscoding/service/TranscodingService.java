package URJC.VideoTranscoding.service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import URJC.VideoTranscoding.codecs.ConversionType;
import URJC.VideoTranscoding.exception.FFmpegException;

/**
 * @author luisca
 */
public interface TranscodingService{
	/**
	 * @param pathFFMPEG
	 * @param fileInput
	 * @param folderOutput
	 * @param conversionType
	 * @throws FFmpegException
	 */
	Map<ConversionType,Boolean> transcode(String pathFFMPEG,File fileInput,Path folderOutput,
				List<ConversionType> conversionType) throws FFmpegException;
}
