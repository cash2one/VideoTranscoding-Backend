package es.urjc.videotranscoding.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import es.urjc.videotranscoding.entities.User;

public interface UserService {
	// TODO JAVADOCs

	User findByEmail(String email);

	List<User> findAllUsers();

	Page<User> findAllUsersPage(Pageable page);

	Optional<User> findOneUser(long id);

	User findOneUser(String nombreUsuario);

	void deleteUsers(long id);

	boolean exists(long id);

	void isAdminVisitorLogged(Principal principal, Model m);

	User save(User u);

	User userVisitor(Principal principal);

	boolean isLogged(Principal principal);

	boolean isAdmin(Principal principal);

	void deleteUser(User u1);

	User registerUser(User u);

	User editUser(User u, long id);

	/**
	 * This method check if the videos that the user have are on filesystem. If not
	 * exists on filesystem, will be deleted for BBDD
	 * 
	 * @param User
	 *            need for check his videos.
	 */
	void checkVideos(User u2);

	void checkVideosForAllUsers();

}
