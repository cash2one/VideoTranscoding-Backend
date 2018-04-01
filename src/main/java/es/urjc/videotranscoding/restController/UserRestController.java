package es.urjc.videotranscoding.restController;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.exception.ExceptionForRest;
import es.urjc.videotranscoding.exception.FFmpegException;
import es.urjc.videotranscoding.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/user")
@Api(tags = "User Api Operations")
@CrossOrigin(origins = "*")
public class UserRestController {
	public interface Details
			extends User.Basic, User.Details, Original.Basic, Original.Details, Conversion.Basic, Conversion.Details {
	}

	@Autowired
	private UserService userService;

	@JsonView(User.Basic.class)
	@ApiOperation(value = "Get all the users")
	@GetMapping(value = "")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<User>> getUsers() {
		List<User> users = userService.findAllUsers();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@JsonView(User.Basic.class)
	@ApiOperation(value = "Delete all the users not admin")
	@DeleteMapping(value = "")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> deleteUsersNotAdmin(Principal principal) {
		if (principal == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		User u = userService.findOneUser(principal.getName());
		if (u.isAdmin()) {
			userService.deleteAllUsers();
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

	}

	@JsonView(Details.class)
	@ApiOperation(value = "Get one user with full details by id and by name")
	@GetMapping(value = "/{id:.*}")
	public ResponseEntity<User> getSingleUser(@PathVariable String id) {
		boolean isEmail = false;
		long idd = 0;
		try {
			idd = Long.parseLong(id);
		} catch (NumberFormatException e) {
			isEmail = true;
		}
		User u = isEmail ? userService.findOneUser(id) : userService.findOneUser(idd);
		if (u != null) {
			return new ResponseEntity<>(u, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Register the new User
	 * 
	 * @param u
	 *            the user new
	 * @param principal
	 *            the user logged.
	 * @return the user created
	 */
	@PostMapping(value = "/register")
	@ApiOperation(value = "Register a new user")
	@JsonView(User.Basic.class)
	public ResponseEntity<?> registerUser(@RequestBody User u, Principal principal) {
		if (principal == null) {
			return new ResponseEntity<User>(userService.registerUser(u), HttpStatus.CREATED);
		} else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	/**
	 * Edit User
	 * 
	 * @param u
	 *            params of the user that you want change
	 * @param id
	 *            of the user to change
	 * @return the new user edited
	 */
	@PatchMapping(value = "/{id}")
	@JsonView(User.Basic.class)
	@ApiOperation(value = "Edit the user by id")
	public ResponseEntity<?> editUser(@RequestBody User u, @PathVariable long id, Principal principal) {
		if (!userService.exists(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		User userOld = userService.findOneUser(id);
		if (principal.getName().equals(userOld.getNick())) {
			User userEdited = userService.editUser(u, id);
			return new ResponseEntity<User>(userEdited, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	/**
	 * Handler for the exceptions
	 * 
	 * @param e
	 *            exception
	 * @return a ExceptionForRest with the exception
	 */
	@ExceptionHandler(FFmpegException.class)
	public ResponseEntity<ExceptionForRest> exceptionHandler(FFmpegException e) {
		ExceptionForRest error = new ExceptionForRest(e.getCodigo(), e.getLocalizedMessage());
		return new ResponseEntity<ExceptionForRest>(error, HttpStatus.BAD_REQUEST);
	}

}
