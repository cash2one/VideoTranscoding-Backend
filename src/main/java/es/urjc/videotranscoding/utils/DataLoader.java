package es.urjc.videotranscoding.utils;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.entities.UserRoles;
import es.urjc.videotranscoding.service.UserService;

/**
 * To charge the users at started the service
 * 
 * @author luisca
 *
 */
@Component
public class DataLoader implements CommandLineRunner {

	@Autowired
	private UserService userService;

	@Resource
	private Properties propertiesFFmpeg;

	public void run(String... strings) throws Exception {
		User u1 = new User("admin@gmail.com", "admin", "pass", "", UserRoles.ADMIN, UserRoles.USER);
		User u2 = new User("user@gmail.com", "user", "pass", "", UserRoles.USER);
		User user1 = userService.findOneUser(u1.getNick());
		User user2 = userService.findOneUser(u2.getNick());
		if (user1 == null) {
			userService.save(u1);
		}
		if (user2 == null) {
			userService.save(u2);
		}
		userService.checkVideosForAllUsers();
	}
}
