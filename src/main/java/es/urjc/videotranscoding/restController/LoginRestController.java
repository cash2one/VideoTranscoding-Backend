package es.urjc.videotranscoding.restController;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.entities.UserComponent;
import io.swagger.annotations.Api;

@RestController
@Api(tags = "Login Controller")
public class LoginRestController {
	private static final Logger log = LoggerFactory
			.getLogger(LoginRestController.class);
	@Autowired
	private UserComponent userComponent;

	@RequestMapping("/logIn")
	public ResponseEntity<User> logIn(Principal principal) {
		if (!userComponent.isLoggedUser()) {
			log.info("Not user logged");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	@RequestMapping("/logOut")
	public ResponseEntity<Boolean> logOut(HttpSession session) {
		if (!userComponent.isLoggedUser()) {
			log.info("No user logged");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			session.invalidate();
			log.info("Logged out");
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
	}
}