package es.urjc.videotranscoding.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.urjc.videotranscoding.entities.Conversion;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion, Long> {
	@Transactional
	void deleteByconversionId(long id);

}
