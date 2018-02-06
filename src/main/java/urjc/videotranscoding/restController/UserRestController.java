package urjc.videotranscoding.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import urjc.videotranscoding.entities.OriginalVideo;
import urjc.videotranscoding.entities.User;
import urjc.videotranscoding.service.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserRestController{
	public interface Details extends User.Basic, User.Details,OriginalVideo.Basic,OriginalVideo.Details{
	}

	@Autowired
	private UserService userService;

	@JsonView(User.Basic.class)
	@RequestMapping(value = "/",method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<User>> getUsers(){
		List<User> users = userService.findAllUsers();
		return new ResponseEntity<>(users,HttpStatus.OK);
	}

	@JsonView(Details.class)
	@RequestMapping(value = "/{id:.*}",method = RequestMethod.GET)
	public ResponseEntity<User> getSingleUser(@PathVariable String id){
		boolean isEmail = false;
		long idd = 0;
		try{
			idd = Long.parseLong(id);
		}catch(NumberFormatException e){
			isEmail = true;
		}
		User u = isEmail ? userService.findOneUser(id) : userService.findOneUser(idd);
		if(u != null){
			return new ResponseEntity<>(u,HttpStatus.OK);
		}else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
