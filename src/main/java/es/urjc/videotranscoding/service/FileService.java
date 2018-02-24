package es.urjc.videotranscoding.service;

import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.exception.FFmpegException;

public interface FileService{
	/**
	 * @param file
	 * @param folderOutput
	 * @return
	 * @throws FFmpegException 
	 */
	public Path saveFile(MultipartFile file) throws FFmpegException;
}
