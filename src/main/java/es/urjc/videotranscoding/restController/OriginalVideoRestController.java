package es.urjc.videotranscoding.restController;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.videotranscoding.entities.ConversionVideo;
import es.urjc.videotranscoding.entities.OriginalVideo;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.service.OriginalVideoService;
import es.urjc.videotranscoding.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/originalVideo")
@Api(tags = "OriginalVideo RestController")
public class OriginalVideoRestController {

	@Autowired
	private OriginalVideoService originalVideoService;
	@Autowired
	private UserService userService;

	public interface Basic extends OriginalVideo.Basic, ConversionVideo.Basic {
	}

	public interface Details extends OriginalVideo.Basic,OriginalVideo.Details, ConversionVideo.Basic ,ConversionVideo.Details{
	}
	/**
	 * All OriginalVideos on the Api
	 * 
	 * @param principal
	 *            the user logger
	 * @return all original videos for all the users
	 */
	@ApiOperation(value = "All OriginalVideos on the Api with their conversions")
	@GetMapping(value = "")
	@JsonView(Basic.class)
	public ResponseEntity<List<OriginalVideo>> getAllVideoConversions(Principal principal) {
		User u = userService.findOneUser(principal.getName());
		if (u == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		List<OriginalVideo> allOriginalVideos = originalVideoService.findAllVideos();
		if (allOriginalVideos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<OriginalVideo>>(allOriginalVideos, HttpStatus.OK);

	}

	/**
	 * Original Video
	 * 
	 * @param principal
	 *            the user logger
	 * @return the original video for this id.
	 */
	@ApiOperation(value = "Original Video for id")
	@GetMapping(value = "/{id}")
	@JsonView(Details.class)
	public ResponseEntity<OriginalVideo> getOriginalVideo(Principal principal, @PathVariable long id) {
		User u = userService.findOneUser(principal.getName());
		if (u == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		OriginalVideo video = originalVideoService.findOneVideo(id);
		if (video == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<OriginalVideo>(video, HttpStatus.OK);

	}

}
