package es.urjc.videotranscoding.restController;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.codecs.ConversionType;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.entities.VideoConversionExpert;
import es.urjc.videotranscoding.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/expert")
@Api(tags = "VideoConversion Expert RestController")
public class VideoConversionRestControllerExpert {
	// TODO JAVADOC
	// @Autowired
	// private VideoConversionService videoConversionService;
	@Autowired
	private UserService userService;

	@ApiOperation(value = "Devuelve las conversiones que tiene")
	@GetMapping(value = "")
	public ResponseEntity<List<ConversionType>> getAllVideoConversions(Principal principal) {
		User u = userService.findOneUser(principal.getName());
		if (u == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		List<ConversionType> conversionTypes = Arrays.asList(ConversionType.values());

		return new ResponseEntity<List<ConversionType>>(conversionTypes, HttpStatus.OK);

	}

	@PostMapping(value = "")
	public ResponseEntity<List<VideoConversionExpert>> addConversionExpert(
			@RequestParam MultiValueMap<String, String> params, @RequestParam(value = "file") MultipartFile file,
			 Principal principal) {
		User u = userService.findOneUser(principal.getName());
		if (u == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<List<VideoConversionExpert>>(HttpStatus.OK);
		// ffmpegService.getAllConversions()
	}

}
