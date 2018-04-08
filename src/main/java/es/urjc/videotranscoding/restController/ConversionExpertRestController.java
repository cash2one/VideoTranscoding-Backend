package es.urjc.videotranscoding.restController;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.videotranscoding.codecs.ConversionType;
import es.urjc.videotranscoding.core.VideoTranscodingService;
import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.exception.ExceptionForRest;
import es.urjc.videotranscoding.exception.FFmpegException;
import es.urjc.videotranscoding.service.OriginalService;
import es.urjc.videotranscoding.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/expert")
@Api(tags = "Conversion Expert Operations")
@CrossOrigin(origins = "*")
public class ConversionExpertRestController {
	@Autowired
	private UserService userService;
	@Autowired
	private OriginalService originalService;
	@Autowired
	private VideoTranscodingService videoTranscodingService;


	public interface Details extends Original.Basic, Original.Details, Conversion.Basic, Conversion.Details {
	}

	/**
	 * Types of conversions for the api expert
	 * 
	 * @param principal
	 *            logged user
	 * @return All types of conversion for the api expert
	 */
	@ApiOperation(value = "Get types of conversions for the api expert")
	@GetMapping(value = "")
	public ResponseEntity<List<ConversionType>> getAllVideoConversionsType(Principal principal) {
		User u = userService.findOneUser(principal.getName());
		if (u == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		List<ConversionType> conversionTypes = Arrays.asList(ConversionType.values());
		return new ResponseEntity<List<ConversionType>>(conversionTypes, HttpStatus.OK);
	}

	/**
	 * Take a MultipartFile and create an original video and her conversions with
	 * the multivaluemap
	 * 
	 * 
	 * @param params
	 *            with the types of conversion(conversionType) videos ready for converter
	 * @param file
	 *            with the file to converter
	 * @param principal
	 *            user Logged
	 * @return the original video with conversionVideos for the video / exception
	 * @throws FFmpegException
	 */
	@PostMapping(value = "")
	@ApiOperation(value = "Send the video(file) and the type of conversions(conversionType) for the video")
	@JsonView(Details.class)	
	public ResponseEntity<?> addConversionExpert(@RequestParam(value="conversionType") List<String> params,
			@RequestParam(value = "file") MultipartFile file,Principal principal) throws FFmpegException {
		User u = userService.findOneUser(principal.getName());
		if (u == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		Original original = originalService.addOriginalExpert(u, file, params);
		videoTranscodingService.transcodeVideo(original);
		return new ResponseEntity<Original>(original, HttpStatus.CREATED);
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
