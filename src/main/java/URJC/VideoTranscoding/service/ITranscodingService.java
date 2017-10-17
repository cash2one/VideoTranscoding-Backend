package URJC.VideoTranscoding.service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface ITranscodingService {
	/**
	 * 
	 * @param pathFFMPEG
	 * @param fileInput
	 * @param folderOutput
	 * @param conversionType
	 */
	void Transcode(String pathFFMPEG, File fileInput, Path folderOutput, List<Integer> conversionType);

}
