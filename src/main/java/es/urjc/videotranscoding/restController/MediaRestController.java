package es.urjc.videotranscoding.restController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.exception.ExceptionForRest;
import es.urjc.videotranscoding.exception.FFmpegException;
import es.urjc.videotranscoding.service.ConversionService;
import es.urjc.videotranscoding.service.OriginalService;
import es.urjc.videotranscoding.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/media")
@Api(tags = "Media Api Operations")
public class MediaRestController {
	// TODO quitar todos los User UNAUTHORIZED

	@Autowired
	private OriginalService originalService;
	@Autowired
	private UserService userService;
	@Autowired
	private ConversionService conversionService;

	public interface Basic extends Original.Basic, Conversion.Basic {
	}

	public interface Details extends Original.Basic, Original.Details, Conversion.Basic, Conversion.Details {
	}

	/**
	 * All Original Videos on the Api
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
	 * @return the video(Original or Conversion) for this id.
	 * @throws FFmpegException
	 */
	@ApiOperation(value = "Get videos information for id")
	@GetMapping(value = "/{id}")
	@JsonView(Details.class)
	public ResponseEntity<?> getOriginalVideo(@PathVariable long id) throws FFmpegException {
		Optional<Original> video = originalService.findOneVideo(id);
		if (!video.isPresent()) {
			return new ResponseEntity<>(getConversionVideo(id), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(video.get(), HttpStatus.OK);
		}
	}

	private Conversion getConversionVideo(long id) throws FFmpegException {
		Optional<Conversion> video = conversionService.findOneConversion(id);
		Conversion conversion = video.get();
		if (conversion == null) {
			//TODO
			throw new FFmpegException(FFmpegException.EX_FFMPEG_EMPTY_OR_NULL);
		}
		return conversion;

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
