package es.urjc.videotranscoding.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.exception.FFmpegException;

public interface FileService{
	/**
	 * @param file
	 * @param folderOutput
	 * @return
	 * @throws FFmpegException 
	 */
	public File saveFile(MultipartFile file) throws FFmpegException;
	//TODO union con fileUtils
}
