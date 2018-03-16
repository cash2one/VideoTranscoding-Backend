package es.urjc.videotranscoding.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.entities.UserRoles;
import es.urjc.videotranscoding.repository.UserRepository;
import es.urjc.videotranscoding.service.FileUtilsFFmpeg;
import es.urjc.videotranscoding.service.OriginalService;
import es.urjc.videotranscoding.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userService;
	@Autowired
	private FileUtilsFFmpeg fileUtilsService;
	@Autowired
	private OriginalService originalService;

	public User findByEmail(String email) {
		return userService.findByEmail(email);
	}

	public List<User> findAllUsers() {
		return userService.findAll();
	}

	public Page<User> findAllUsersPage(Pageable page) {
		return userService.findAll(page);
	}

	public Optional<User> findOneUser(long id) {
		return userService.findById(id);
	}

	public User findOneUser(String nombreUsuario) {
		return userService.findByNick(nombreUsuario);
	}

	public void deleteUsers(long id) {
		userService.deleteById(id);
	}

	public void deleteUser(User u1) {
		userService.delete(u1);
	}

	public boolean exists(long id) {
		return userService.existsById(id);
	}

	public void save(User u) {
		userService.save(u);
	}

	public boolean isLogged(Principal principal) {
		if (principal == null)
			return false;
		return !(userService.findByEmail(principal.getName()) == null);
	}

	public boolean isAdmin(Principal principal) {
		if (principal == null)
			return false;
		User u = userService.findByEmail(principal.getName());
		if (u == null)
			return false;
		return u.isAdmin();
	}

	public User registerUser(User u) {
		User newUser = new User(u.getEmail(), u.getNick(), u.getHashedPassword(), u.getPhoto(), UserRoles.USER);
		save(newUser);
		return newUser;
	}

	public User editUser(User u, long id) {
		Optional<User> userToEditedOptional = findOneUser(id);
		User userToEdited = userToEditedOptional.get();
		if (u.getEmail() != null) {
			userToEdited.setEmail(u.getEmail());
		}
		if (u.getNick() != null) {
			userToEdited.setNick(u.getNick());
		}
		if (u.getHashedPassword() != null) {
			userToEdited.changePassword(u.getHashedPassword());
		}
		save(userToEdited);
		return userToEdited;
	}

	public void checkVideos(User u) {
		List<Original> listToRemove = new ArrayList<>();
		for (Original video : u.getListVideos()) {
			if (!fileUtilsService.exitsFile(video.getPath())) {
				listToRemove.add(video);
			}
		}
		if (!listToRemove.isEmpty()) {
			originalService.deleteVideos(u, listToRemove);
		}
	}

	public void checkVideosForAllUsers() {
		for (User user : findAllUsers()) {
			checkVideos(user);
		}
	}

}
