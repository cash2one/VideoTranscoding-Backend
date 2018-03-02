package es.urjc.videotranscoding.service;

import java.util.List;
import java.util.Optional;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.codecs.ConversionType;
import es.urjc.videotranscoding.entities.OriginalVideo;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.exception.FFmpegException;

public interface OriginalVideoService {
	//TODO Javadoc
	void save(OriginalVideo video);

	void delete(OriginalVideo video);

	void delete(long id);

	List<OriginalVideo> findAllVideos();
	
	OriginalVideo addOriginalVideoExpert(User u,MultipartFile file,MultiValueMap<String, String> params) throws FFmpegException;

	OriginalVideo addOriginalVideoBasic(User u, MultipartFile file, List<ConversionType> type) throws FFmpegException;

	Optional<OriginalVideo> findOneVideo(long id);
}
