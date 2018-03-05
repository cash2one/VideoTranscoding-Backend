package es.urjc.videotranscoding.restController;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.service.OriginalService;
import es.urjc.videotranscoding.service.UserService;
import es.urjc.videotranscoding.utils.FileSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/originalVideo")
@Api(tags = "Original Video Api Operations")
public class OriginalRestController {

	@Autowired
	private OriginalService originalService;
	@Autowired
	private UserService userService;

	public interface Basic extends Original.Basic, Conversion.Basic {
	}

	public interface Details
			extends Original.Basic, Original.Details, Conversion.Basic, Conversion.Details {
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
	public ResponseEntity<List<Original>> getAllVideoConversions(Principal principal) {
		User u = userService.findOneUser(principal.getName());
		if (u == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		List<Original> allOriginalVideos = originalService.findAllVideos();
		if (allOriginalVideos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Original>>(allOriginalVideos, HttpStatus.OK);

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
	public ResponseEntity<Optional<Original>> getOriginalVideo(Principal principal, @PathVariable long id) {
		User u = userService.findOneUser(principal.getName());
		if (u == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		Optional<Original> video = originalService.findOneVideo(id);
		if (video == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Optional<Original>>(video, HttpStatus.OK);

	}

	/**
	 * With this method you can see the original video
	 * 
	 * @param response
	 *            for the video
	 * @param request
	 *            for the video
	 * @param id
	 *            of the original Video
	 * @return
	 */
	@ApiOperation(value = "Watch the Original Video")
	@GetMapping(value = "/{id}/watch")
	public ResponseEntity<?> downloadDirectFilm2(HttpServletResponse response, HttpServletRequest request,
			@PathVariable long id) {

		Optional<Original> video = originalService.findOneVideo(id);

		if (video != null) {
			video.get();
			Path p1 = Paths.get(video.get().getPath());

			FileSender.fromPath(p1).with(request).with(response).serveResource();

			return new ResponseEntity<Original>(HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}

}
