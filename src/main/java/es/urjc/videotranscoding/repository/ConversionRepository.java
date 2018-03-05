package es.urjc.videotranscoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.urjc.videotranscoding.entities.Conversion;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion, Long> {

}
