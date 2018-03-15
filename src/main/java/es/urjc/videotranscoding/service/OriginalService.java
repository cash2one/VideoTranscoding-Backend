package es.urjc.videotranscoding.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.exception.FFmpegException;

public interface OriginalService {
	// TODO Javadoc
	void save(Original video);

	User deleteOriginal(Original original, User u);

	void deleteAllVideosByAdmin();
	
	User deleteAllVideos(User u);

	List<Original> findAllVideos();

	Original addOriginalExpert(User u, MultipartFile file, List<String> params) throws FFmpegException;

	Original addOriginalBasic(User u, MultipartFile file, List<String> params) throws FFmpegException;

	Optional<Original> findOneVideo(long id);

}
