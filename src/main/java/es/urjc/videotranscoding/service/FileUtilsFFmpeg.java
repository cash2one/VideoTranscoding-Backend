package es.urjc.videotranscoding.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.exception.FFmpegException;

public interface FileUtilsFFmpeg {

	boolean exitsFile(String file);
	boolean exitsPath(String path);
	/**
	 * @param file
	 * @param folderOutput
	 * @return
	 * @throws FFmpegException 
	 */
	File saveFile(MultipartFile file) throws FFmpegException;
}
