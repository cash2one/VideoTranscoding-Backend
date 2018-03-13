package es.urjc.videotranscoding.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.exception.FFmpegException;

public interface FileUtilsFFmpeg {
	// TODO JAVADOC

	/**
	 * 
	 * @param file
	 * @return
	 */
	boolean exitsFile(String file);

	/**
	 * 
	 * @param path
	 * @return
	 */
	boolean exitsPath(String path);

	/**
	 * @param file
	 * @param folderOutput
	 * @return
	 * @throws FFmpegException
	 */
	File saveFile(MultipartFile file) throws FFmpegException;

	/**
	 * 
	 * @param file
	 * @return
	 */
	boolean deleteFile(String file);
}
