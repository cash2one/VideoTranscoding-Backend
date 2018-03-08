package es.urjc.videotranscoding.core.impl;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.repository.ConversionRepository;
import es.urjc.videotranscoding.wrapper.FfmpegResourceBundle;

@Component
public class StreamGobblerFactory {
	private static final String FICH_TRAZAS = "fichero.mensajes.trazas";
	private static final Logger logger = Logger.getLogger(StreamGobblerFactory.class);

	@Autowired
	private ConversionRepository conversionRepository;
	@Resource
	private FfmpegResourceBundle ffmpegResourceBundle;
	@Resource
	private Properties propertiesFicheroCore;

	public StreamGobbler getStreamGobblerPersistent(InputStream is, String type, Conversion conversion) {
		return new StreamGobbler(is, type, conversion, conversionRepository, logger);

	}
	@PostConstruct
	public void init() {
		logger.setResourceBundle(ffmpegResourceBundle
				.getFjResourceBundle(propertiesFicheroCore.getProperty(FICH_TRAZAS), Locale.getDefault()));
	}
}
