package urjc.videotranscoding.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import urjc.videotranscoding.entities.VideoConversion;
import urjc.videotranscoding.repository.VideoConversionRepository;

@Service
public class FFmpegService {
	@Autowired
	private VideoConversionRepository videoConversionRepository;

	public List<VideoConversion> getAllConversions() {
		return videoConversionRepository.findAll();
	}
}
