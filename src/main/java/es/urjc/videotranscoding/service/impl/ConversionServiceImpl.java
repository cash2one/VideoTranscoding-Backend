package es.urjc.videotranscoding.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.repository.ConversionRepository;
import es.urjc.videotranscoding.service.ConversionService;
import es.urjc.videotranscoding.service.FileUtilsFFmpeg;
import es.urjc.videotranscoding.service.OriginalService;

@Service
public class ConversionServiceImpl implements ConversionService {

	@Autowired
	private ConversionRepository conversionRepository;
	@Autowired
	private FileUtilsFFmpeg fileUtilsService;
	@Autowired
	private OriginalService originalService;

	public void save(Conversion video) {
		conversionRepository.save(video);
	}

	public void delete(Conversion video) {
		conversionRepository.delete(video);
	}

	public void delete(long id) {
		conversionRepository.deleteById(id);
	}

	@Override
	public Optional<Conversion> findOneConversion(long id) {
		return conversionRepository.findById(id);
	}

	@Override
	public User deleteConversion(Conversion video, User u) {
		if (u.isAdmin()) {
			video.getParent().removeConversion(video);
			save(video);
			originalService.save(video.getParent());
			fileUtilsService.deleteFile(video.getPath());
			conversionRepository.deleteByconversionId(video.getConversionId());
		} else {
			for (Original iterator : u.getListVideos()) {
				if (iterator.getOriginalId().equals(video.getConversionId())) {
					video.getParent().removeConversion(video);
					originalService.save(video.getParent());
					fileUtilsService.deleteFile(video.getPath());
					conversionRepository.deleteByconversionId(video.getConversionId());
				}
			}
		}
		return u;
	}

}
