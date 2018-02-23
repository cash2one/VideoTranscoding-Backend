package es.urjc.videotranscoding.service;

import es.urjc.videotranscoding.entities.ConversionVideo;

public interface ConversionVideoService {

	public void save(ConversionVideo video);

	public void delete(ConversionVideo video);

	public void delete(long id);

}
