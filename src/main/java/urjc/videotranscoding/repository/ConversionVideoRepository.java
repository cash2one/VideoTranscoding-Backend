package urjc.videotranscoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import urjc.videotranscoding.entities.ConversionVideo;

@Repository
public interface ConversionVideoRepository extends JpaRepository<ConversionVideo, Long> {

}
