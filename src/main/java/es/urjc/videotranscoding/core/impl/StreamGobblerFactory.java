package es.urjc.videotranscoding.core.impl;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.urjc.videotranscoding.entities.ConversionVideo;
import es.urjc.videotranscoding.repository.ConversionVideoRepository;

@Component
public class StreamGobblerFactory {

	@Autowired
	private ConversionVideoRepository conversionVideoService;

	public StreamGobbler getStreamGobblerPersistent(InputStream is, String type,
			ConversionVideo conversionVideo){
		
		return new StreamGobbler(is, type, conversionVideo, conversionVideoService);
		
	}
}
