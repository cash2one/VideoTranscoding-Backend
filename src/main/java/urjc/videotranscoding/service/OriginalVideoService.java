package urjc.videotranscoding.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import urjc.videotranscoding.entities.OriginalVideo;
import urjc.videotranscoding.repository.OriginalVideoRepository;

@Service
public class OriginalVideoService {
	@Autowired
	private OriginalVideoRepository originalVideoRepository;

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
		// TODO Auto-generated method stub
		return originalVideoRepository.findAll();
	}
}
