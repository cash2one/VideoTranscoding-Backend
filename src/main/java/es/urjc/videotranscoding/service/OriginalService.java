package es.urjc.videotranscoding.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.exception.FFmpegException;

public interface OriginalService {
	/**
	 * Save the video on BBDD
	 * 
	 * @param video
	 *            to saved.
	 */
	void save(Original video);

	/**
	 * Delete the video and refresh the user
	 * 
	 * @param original
	 *            with contains the video that you want delete.
	 * @param u
	 *            that contains the video
	 * @return the user updated.
	 */
	User deleteOriginal(Original original, User u);

	/**
	 * Only by ADMIN. With this method you can delete ALL the videos of all the
	 * users for clean the BBDD and filesystem.
	 */
	void deleteAllVideosByAdmin();

	/**
	 * Delete all videos of the user u.
	 * 
	 * @param u
	 *            that you will be delete his videos.
	 * @return the user updated.
	 */
	User deleteAllVideos(User u);

	/**
	 * Delete a list of videos .
	 * 
	 * @param u
	 *            with the user of his videos
	 * @param listOriginal
	 *            with the videos to delete.
	 * @return the user updated.
	 */
	User deleteVideos(User u, List<Original> listOriginal);

	/**
	 * Find ALL the videos on BBDD
	 * 
	 * @return the list with the all the videos
	 */
	List<Original> findAllVideos();

	/**
	 * Add an original video and his conversions
	 * 
	 * @param u
	 *            that is the propietary of the video
	 * @param file
	 *            with video with his original name
	 * @param params
	 *            with the conversions.
	 * @return A original created and saved on BBDD
	 * @throws FFmpegException
	 */
	Original addOriginalExpert(User u, MultipartFile file, List<String> params) throws FFmpegException;

	/**
	 * Add an original video and his basic conversions (VLC, WEB,MOVIL, ALL)
	 * 
	 * @param u
	 *            propietary of the video
	 * @param file
	 *            with the video with his original name
	 * @param params
	 *            with the type of converion
	 * @return an original video created and saved on BBDD
	 * @throws FFmpegException
	 */
	Original addOriginalBasic(User u, MultipartFile file, List<String> params) throws FFmpegException;

	/**
	 * Find one video with the id and.
	 * 
	 * @param id
	 *            for find the video
	 * @return an optional that can contains the original or no.
	 */
	Optional<Original> findOneVideo(long id);

}
