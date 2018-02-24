package es.urjc.videotranscoding.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.entities.OriginalVideo;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.exception.FFmpegException;
import es.urjc.videotranscoding.repository.OriginalVideoRepository;
import es.urjc.videotranscoding.service.FileService;
import es.urjc.videotranscoding.service.OriginalVideoService;

@Service
public class OriginalVideoServiceImpl implements OriginalVideoService {
	@Autowired
	private OriginalVideoRepository originalVideoRepository;
	@Autowired 
private FileService fileService;
	public void save(OriginalVideo video) {
		originalVideoRepository.save(video);
	}

	public void delete(OriginalVideo video) {
		originalVideoRepository.delete(video);
	}

	public void delete(long id) {
		originalVideoRepository.delete(id);
	}

	public List<OriginalVideo> findAllVideos() {
		return originalVideoRepository.findAll();
	}

	@Override
	public OriginalVideo addOriginalVideo(User u, MultipartFile file, MultiValueMap<String, String> params) throws FFmpegException {
		fileService.saveFile(file);
		//OriginalVideo originalVideo= new OriginalVideo(name, path, user);
		return null;
	}
	
}
