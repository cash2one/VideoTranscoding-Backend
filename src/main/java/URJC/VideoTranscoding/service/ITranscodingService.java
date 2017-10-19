package URJC.VideoTranscoding.service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import URJC.VideoTranscoding.exception.FFmpegException;

public interface ITranscodingService {
	/**
	 * 
	 * @param pathFFMPEG
	 * @param fileInput
	 * @param folderOutput
	 * @param conversionType
	 * @throws FFmpegException
	 */
	void Transcode(String pathFFMPEG, File fileInput, Path folderOutput, List<Integer> conversionType) throws FFmpegException;

}
