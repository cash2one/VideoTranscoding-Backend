package es.urjc.videotranscoding.service;

import java.util.List;
import java.util.Optional;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.codecs.ConversionType;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.exception.FFmpegException;

public interface OriginalService {
	//TODO Javadoc
	void save(Original video);

	void delete(Original video);

	void delete(long id);

	List<Original> findAllVideos();
	
	Original addOriginalExpert(User u,MultipartFile file,MultiValueMap<String, String> params) throws FFmpegException;

	Original addOriginalBasic(User u, MultipartFile file, List<ConversionType> type) throws FFmpegException;

	Optional<Original> findOneVideo(long id);
}
