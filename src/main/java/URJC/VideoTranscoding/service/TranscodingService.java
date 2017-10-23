package URJC.VideoTranscoding.service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

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
	void transcode(String pathFFMPEG,File fileInput,Path folderOutput,List<ConversionType> conversionType)
				throws FFmpegException;
}
