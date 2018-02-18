package urjc.videotranscoding.service;

import urjc.videotranscoding.entities.ConversionVideo;

public interface ConversionVideoService {

	public void save(ConversionVideo video);

	public void delete(ConversionVideo video);

	public void delete(long id);

}
