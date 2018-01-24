package urjc.videotranscoding.service;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import urjc.videotranscoding.entities.User;
import urjc.videotranscoding.repository.UserRepository;


@Service
public class UserService{
	@Autowired
	UserRepository users;


	public User findByEmail(String email){
		return users.findByEmail(email);
	}

	public List<User> findAllUsers(){
		return users.findAll();
	}

	public Page<User> findAllUsersPage(Pageable page){
		return users.findAll(page);
	}

	public User findOneUser(long id){
		return users.findOne(id);
	}
	public User findOneUser(String nombreUsuario) {
		return  users.findByNick(nombreUsuario);
	}

	public void deleteUsers(long id){
		users.delete(id);
	}

	public boolean exists(long id){
		return users.exists(id);
	}

	public void isAdminVisitorLogged(Principal principal,Model m){
		boolean isLogged = principal != null;
		User visitor = (isLogged) ? users.findByEmail(principal.getName()) : null;
		m.addAttribute("visitor",visitor);
		m.addAttribute("isLogged",isLogged);
		m.addAttribute("isAdmin",(visitor != null && visitor.isAdmin()));
	}

	public User save(User u){
		return users.save(u);
	}

	public User userVisitor(Principal principal){
		User user = users.findByEmail(principal.getName());
		return user;
	}

	public boolean isLogged(Principal principal){
		if(principal == null)
			return false;
		return !(users.findByEmail(principal.getName()) == null);
	}

	public boolean isAdmin(Principal principal){
		if(principal == null)
			return false;
		User u = users.findByEmail(principal.getName());
		if(u == null)
			return false;
		return u.isAdmin();
	}

}