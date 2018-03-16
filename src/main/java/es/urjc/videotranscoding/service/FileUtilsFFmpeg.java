package es.urjc.videotranscoding.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.exception.FFmpegException;

public interface FileUtilsFFmpeg {

	/**
	 * Check if the file exits
	 * 
	 * @param file
	 *            with the path of the file
	 * @return boolean.
	 */
	boolean exitsFile(String file);

	/**
	 * Check if the path exits on fileSystem
	 * 
	 * @param path
	 *            with the path of the directory
	 * @return boolean.
	 */
	boolean exitsDirectory(String path);

	/**
	 * Save a multipart on the fileSystem. The path is predefined and can be change
	 * on properties
	 * 
	 * @param file
	 *            that contains the video.
	 * @return the video saved.
	 * @throws FFmpegException
	 */
	File saveFile(MultipartFile file) throws FFmpegException;

	/**
	 * Delete a file
	 * 
	 * @param file
	 *            that will be deleted
	 * @return boolean.
	 */
	boolean deleteFile(String file);
}
