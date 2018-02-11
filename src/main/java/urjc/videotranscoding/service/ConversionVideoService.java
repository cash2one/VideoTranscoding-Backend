package urjc.videotranscoding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import urjc.videotranscoding.entities.ConversionVideo;
import urjc.videotranscoding.repository.ConversionVideoRepository;

@Service
public class ConversionVideoService {
	@Autowired
	private ConversionVideoRepository conversionVideoRepository;

	public void save(ConversionVideo video) {
		conversionVideoRepository.save(video);
	}

	public void delete(ConversionVideo video) {
		conversionVideoRepository.delete(video);
	}

	public void delete(long id) {
		conversionVideoRepository.delete(id);
	}

}
