package urjc.videotranscoding.restController;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import urjc.videotranscoding.entities.User;
import urjc.videotranscoding.entities.VideoConversion;
import urjc.videotranscoding.service.FFmpegService;
import urjc.videotranscoding.service.UserService;

@RestController
@RequestMapping(value = "/api")
@Api(tags = "FFmpeg RestController")
public class FFmpegRestController {
	// TODO JAVADOC
	@Autowired
	private FFmpegService ffmpegService;
	@Autowired
	private UserService userService;
	@ApiOperation(value = "Devuelve las conversiones que tiene")
	@GetMapping(value = "/")
	public ResponseEntity<List<VideoConversion>> getAllVideoConversions(
			Principal principal) {
		User u = userService.findOneUser(principal.getName());
		if (u == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<List<VideoConversion>>(
				ffmpegService.getAllConversions(), HttpStatus.OK);

	}
}
