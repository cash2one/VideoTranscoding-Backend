package es.urjc.videotranscoding.service;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.exception.FFmpegException;

public interface UserService {

	public User findByEmail(String email);

	public List<User> findAllUsers();

	public Page<User> findAllUsersPage(Pageable page);

	public User findOneUser(long id);

	public User findOneUser(String nombreUsuario);

	public void deleteUsers(long id);

	public boolean exists(long id);

	public void isAdminVisitorLogged(Principal principal, Model m);

	public User save(User u);

	public User userVisitor(Principal principal);

	public boolean isLogged(Principal principal);

	public boolean isAdmin(Principal principal);

	// TODO ESTE METODO ES AUTOMATICO
	void callTranscodeIfChargeIsDown() throws FFmpegException;

}
