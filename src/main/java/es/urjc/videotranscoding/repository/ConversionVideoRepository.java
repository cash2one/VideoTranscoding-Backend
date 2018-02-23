package es.urjc.videotranscoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.urjc.videotranscoding.entities.ConversionVideo;

@Repository
public interface ConversionVideoRepository extends JpaRepository<ConversionVideo, Long> {

}
