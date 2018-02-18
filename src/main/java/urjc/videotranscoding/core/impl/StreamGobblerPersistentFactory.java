package urjc.videotranscoding.core.impl;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import urjc.videotranscoding.entities.ConversionVideo;
import urjc.videotranscoding.repository.ConversionVideoRepository;

@Component
public class StreamGobblerPersistentFactory {

	@Autowired
	private ConversionVideoRepository conversionVideoService;

	public StreamGobblerPersistent getStreamGobblerPersistent(InputStream is, String type,
			ConversionVideo conversionVideo){
		
		return new StreamGobblerPersistent(is, type, conversionVideo, conversionVideoService);
		
	}
}
