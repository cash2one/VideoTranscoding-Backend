package urjc.videotranscoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import urjc.videotranscoding.entities.VideoConversion;

public interface VideoConversionRepository
		extends
			JpaRepository<VideoConversion, Long> {

}
