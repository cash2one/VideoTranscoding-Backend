package es.urjc.videotranscoding.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.urjc.videotranscoding.entities.OriginalVideo;
import es.urjc.videotranscoding.repository.OriginalVideoRepository;
import es.urjc.videotranscoding.service.OriginalVideoService;

@Service
public class OriginalVideoServiceImpl implements OriginalVideoService {
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
		return originalVideoRepository.findAll();
	}
}
