package urjc.videotranscoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import urjc.videotranscoding.entities.OriginalVideo;
@Repository
public interface OriginalVideoRepository
		extends
			JpaRepository<OriginalVideo, Long> {

}
