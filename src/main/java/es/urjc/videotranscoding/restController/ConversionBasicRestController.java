package es.urjc.videotranscoding.restController;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.videotranscoding.codecs.ConversionTypeBasic;
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
@RequestMapping(value = "/api/basic")
@Api(tags = "Conversion Basic Operations")
public class ConversionBasicRestController {
	@Autowired
	private UserService userService;
	@Autowired
	private OriginalService originalService;
	@Autowired
	private VideoTranscodingService videoTranscodingService;

	public interface Details extends Original.Basic, Original.Details, Conversion.Basic, Conversion.Details {
	}

	/**
	 * Get the types of conversion
	 * 
	 * @param principal
	 *            user Logged
	 * @return the list of conversion basic
	 * @throws FFmpegException
	 */
	@GetMapping(value = "")
	@ApiOperation(value = "Get the types of conversion")
	public ResponseEntity<List<String>> getConversionBasic(Principal principal) throws FFmpegException {
		User u = userService.findOneUser(principal.getName());
		if (u == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		List<String> typeConversionBasic = ConversionTypeBasic.getAllTypesBasic();
		return new ResponseEntity<>(typeConversionBasic, HttpStatus.OK);
	}

	/**
	 * Take a MultipartFile and create an original video and her conversions with
	 * list of conversion as params
	 * 
	 * 
	 * @param params
	 *            with the types of conversion videos ready for converter
	 * @param file
	 *            with the file to converter
	 * @param principal
	 *            user Logged
	 * @return the original video with conversionVideos for the video / exception
	 * @throws FFmpegException
	 */
	@PostMapping(value = "")
	@ApiOperation(value = "Send the video for conversion")
	@JsonView(Details.class)
	public ResponseEntity<Object> addConversionBasic(@RequestParam(value = "file") MultipartFile file,
			@RequestParam(value = "conversionType") List<String> conversionList, Principal principal) throws FFmpegException {
		User u = userService.findOneUser(principal.getName());
		if (u == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		Original original = originalService.addOriginalBasic(u, file, conversionList);
		videoTranscodingService.transcodeVideo(original);
		return new ResponseEntity<>(original, HttpStatus.CREATED);
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
