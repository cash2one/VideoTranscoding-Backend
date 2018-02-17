package urjc.videotranscoding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import urjc.videotranscoding.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	List<User> findLast5ByNick(String nick);

	User findByNick(String id);

}
