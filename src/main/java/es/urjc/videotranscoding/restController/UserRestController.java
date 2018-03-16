package es.urjc.videotranscoding.restController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
@RequestMapping(value = "/api/user")
@Api(tags = "User Api Operations")
public class UserRestController {
	public interface Details
			extends User.Basic, User.Details, Original.Basic, Original.Details, Conversion.Basic, Conversion.Details {
	}

	@Autowired
	private UserService userService;

	@JsonView(User.Basic.class)
	@ApiOperation(value = "Get all the users")
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<User>> getUsers() {
		List<User> users = userService.findAllUsers();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@JsonView(Details.class)
	@ApiOperation(value = "Get one user with full details by id")
	@GetMapping(value = "/{id}")
	public ResponseEntity<User> getSingleUser(@PathVariable long id) {
		Optional<User> u = userService.findOneUser(id);

		if (u != null) {
			return new ResponseEntity<>(u.get(), HttpStatus.OK);
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
		if (principal.getName() == null) {
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
		Optional<User> userOld = userService.findOneUser(id);
		if (principal.getName().equals(userOld.get().getNick())) {
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
