package es.urjc.videotranscoding.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;

@Repository
public interface OriginalRepository extends PagingAndSortingRepository<Original, Long> {
	Page<Original> findAllByUserVideo(Pageable page, User u);

	Page<Original> findAll(Pageable pageable);

}
