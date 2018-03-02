package es.urjc.videotranscoding.service.impl;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import es.urjc.videotranscoding.core.VideoTranscodingService;
import es.urjc.videotranscoding.entities.OriginalVideo;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.exception.FFmpegException;
import es.urjc.videotranscoding.repository.UserRepository;
import es.urjc.videotranscoding.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Resource
	private Properties propertiesFFmpeg;
	@Autowired
	private VideoTranscodingService transcode;
	@Autowired
	private UserRepository users;

	public User findByEmail(String email) {
		return users.findByEmail(email);
	}

	public List<User> findAllUsers() {
		return users.findAll();
	}

	public Page<User> findAllUsersPage(Pageable page) {
		return users.findAll(page);
	}

	public Optional<User> findOneUser(long id) {
		return users.findById(id);
	}

	public User findOneUser(String nombreUsuario) {
		return users.findByNick(nombreUsuario);
	}

	public void deleteUsers(long id) {
		users.deleteById(id);
	}

	public boolean exists(long id) {
		return users.existsById(id);
	}

	public void isAdminVisitorLogged(Principal principal, Model m) {
		boolean isLogged = principal != null;
		User visitor = (isLogged) ? users.findByEmail(principal.getName()) : null;
		m.addAttribute("visitor", visitor);
		m.addAttribute("isLogged", isLogged);
		m.addAttribute("isAdmin", (visitor != null && visitor.isAdmin()));
	}

	public User save(User u) {
		return users.save(u);
	}

	public User userVisitor(Principal principal) {
		User user = users.findByEmail(principal.getName());
		return user;
	}

	public boolean isLogged(Principal principal) {
		if (principal == null)
			return false;
		return !(users.findByEmail(principal.getName()) == null);
	}

	public boolean isAdmin(Principal principal) {
		if (principal == null)
			return false;
		User u = users.findByEmail(principal.getName());
		if (u == null)
			return false;
		return u.isAdmin();
	}

	// @Scheduled(cron = "*/10 * * * * *")
	public void callTranscodeIfChargeIsDown() throws FFmpegException, InterruptedException, ExecutionException {
		for (User user : users.findAll()) {
			for (OriginalVideo originalVideo : user.getListVideos()) {
				if (!originalVideo.isActive() && !originalVideo.isComplete()) {
					transcode.transcodeVideo(originalVideo);

				}
			}

		}

	}
}
