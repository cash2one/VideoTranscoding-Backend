package es.urjc.videotranscoding.service;

import es.urjc.videotranscoding.entities.Conversion;

public interface ConversionService {

	public void save(Conversion video);

	public void delete(Conversion video);

	public void delete(long id);

}
