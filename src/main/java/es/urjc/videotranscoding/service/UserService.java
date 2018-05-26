package es.urjc.videotranscoding.service;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.urjc.videotranscoding.entities.User;

public interface UserService {
	/**
	 * Find a user for the email
	 * 
	 * @param email
	 *            for the search
	 * @return the user or null that found.
	 */
	User findByEmail(String email);

	/**
	 * Find all the users that are on BBDD
	 * 
	 * @return all the users of the BBDD
	 */
	List<User> findAllUsers();

	/**
	 * Pageable for the users
	 * 
	 * @param page
	 *            param for the pageable
	 * @return a pageable with the user
	 */
	Page<User> findAllUsersPage(Pageable page);

	/**
	 * Find a user with the id
	 * 
	 * @param id
	 *            need for the search
	 * @return the optional that can contains the user or no
	 */
	User findOneUser(long id);

	/**
	 * Find a user with the nameUser(nick)
	 * 
	 * @param nombreUsuario
	 *            for the search of the user
	 * @return a User or null found
	 */
	User findOneUser(String nombreUsuario);

	/**
	 * Delete users with the id
	 * 
	 * @param id
	 *            for delete the user
	 */
	void deleteUsers(long id);

	/**
	 * Check if the users exists on BBDD
	 * 
	 * @param id
	 *            for check if exists
	 * @return boolean;
	 */
	boolean exists(long id);

	/**
	 * Save the user on BBDD
	 * 
	 * @param u
	 *            the user that you want
	 */
	void save(User u);

	/**
	 * Check if a user is logged on app
	 * 
	 * @param principal
	 *            with the session of the user
	 * @return boolean
	 */
	boolean isLogged(Principal principal);

	/**
	 * Check if the user logged is admin or no
	 * 
	 * @param principal
	 *            with the session of the user
	 * @return boolean
	 */
	boolean isAdmin(Principal principal);

	/**
	 * Delete user
	 * 
	 * @param u1
	 *            the user to will be deletes
	 */
	void deleteUser(User u1);

	/**
	 * Register a new user
	 * 
	 * @param u
	 *            to be saved on BBDD
	 * @return the user created and saved on BBD
	 */
	User registerUser(User u);

	/**
	 * Edit the user
	 * 
	 * @param u
	 *            with the body and fields that wants edit
	 * @param id
	 *            of the user old.
	 * @return the user edited and saved
	 */
	User editUser(User u, long id);

	/**
	 * This method check if the videos that the user have are on filesystem. If not
	 * exists on filesystem, will be deleted for BBDD
	 * 
	 * @param u2
	 *            User need for check his videos.
	 */
	void checkVideos(User u2);

	/**
	 * Check all the videos for all the users.
	 */
	void checkVideosForAllUsers();

	/**
	 * Delete all users for clean the BBDD
	 */
	void deleteAllUsers();

}
