package es.urjc.videotranscoding.restController;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.entities.UserComponent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Login Operations")
public class LoginRestController {
	private static final Logger log = LoggerFactory.getLogger(LoginRestController.class);
	@Autowired
	private UserComponent userComponent;

	/**
	 * Log in in the service
	 * 
	 * @param principal
	 * @return the user logged
	 */
	@GetMapping("/logIn")
	@ApiOperation(value = "Log in the Service")
	public ResponseEntity<User> logIn(Principal principal) {
		if (!userComponent.isLoggedUser()) {
			log.info("Not user logged");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	/**
	 * Log out of the service
	 * 
	 * @param session
	 * @return true or false if you log off
	 */
	@GetMapping("/logOut")
	@ApiOperation(value = "Log out the Service")
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