package es.urjc.videotranscoding.core.impl;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.repository.ConversionRepository;

@Component
public class StreamGobblerFactory {

	@Autowired
	private ConversionRepository conversionService;

	public StreamGobbler getStreamGobblerPersistent(InputStream is, String type,
			Conversion conversion){
		
		return new StreamGobbler(is, type, conversion, conversionService);
		
	}
}
