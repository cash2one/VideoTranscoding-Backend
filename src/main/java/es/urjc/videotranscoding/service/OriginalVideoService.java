package es.urjc.videotranscoding.service;

import java.util.List;

import es.urjc.videotranscoding.entities.OriginalVideo;

public interface OriginalVideoService {

	public void save(OriginalVideo video);

	public void delete(OriginalVideo video);

	public void delete(long id);

	public List<OriginalVideo> findAllVideos();
}
