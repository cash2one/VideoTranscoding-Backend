package es.urjc.videotranscoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.urjc.videotranscoding.entities.Original;

@Repository
public interface OriginalRepository extends JpaRepository<Original, Long> {

	
}
