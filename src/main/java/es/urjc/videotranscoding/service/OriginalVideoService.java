package es.urjc.videotranscoding.service;

import java.util.List;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.entities.OriginalVideo;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.exception.FFmpegException;

public interface OriginalVideoService {

	void save(OriginalVideo video);

	void delete(OriginalVideo video);

	void delete(long id);

	List<OriginalVideo> findAllVideos();
	
	OriginalVideo addOriginalVideo(User u,MultipartFile file,MultiValueMap<String, String> params) throws FFmpegException;
}
