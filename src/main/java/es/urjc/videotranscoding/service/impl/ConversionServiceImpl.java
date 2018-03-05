package es.urjc.videotranscoding.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.repository.ConversionRepository;
import es.urjc.videotranscoding.service.ConversionService;

@Service
public class ConversionServiceImpl implements ConversionService {
	@Autowired
	private ConversionRepository conversionRepository;

	public void save(Conversion video) {
		conversionRepository.save(video);
	}

	public void delete(Conversion video) {
		conversionRepository.delete(video);
	}

	public void delete(long id) {
		conversionRepository.deleteById(id);
	}

}
