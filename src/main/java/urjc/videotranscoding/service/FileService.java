package urjc.videotranscoding.service;

import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

public interface FileService{
	/**
	 * @param file
	 * @param folderOutput
	 * @return
	 */
	public Path saveFile(MultipartFile file,String folderOutput);
}
