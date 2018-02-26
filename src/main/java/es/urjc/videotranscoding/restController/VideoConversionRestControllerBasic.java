package es.urjc.videotranscoding.restController;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.videotranscoding.codecs.ConversionTypeBasic;
import es.urjc.videotranscoding.core.VideoTranscodingService;
import es.urjc.videotranscoding.entities.ConversionVideo;
import es.urjc.videotranscoding.entities.OriginalVideo;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.exception.ExceptionForRest;
import es.urjc.videotranscoding.exception.FFmpegException;
import es.urjc.videotranscoding.service.OriginalVideoService;
import es.urjc.videotranscoding.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/basic")
@Api(tags = "VideoConversion Basic RestController")
public class VideoConversionRestControllerBasic {
	@Autowired
	private UserService userService;
	@Autowired
	private OriginalVideoService originalVideoService;
	@Autowired
	private VideoTranscodingService videoTranscodingService;

	public interface Details
			extends OriginalVideo.Basic, OriginalVideo.Details, ConversionVideo.Basic, ConversionVideo.Details {
	}

	/**
	 * Take a MultipartFile and create an original video and her conversions with
	 * the multivaluemap
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
	@PostMapping(value = "/web")
	@ApiOperation(value = "Send the video for conversion")
	@JsonView(Details.class)
	@ResponseBody
	public ResponseEntity<Object> addConversionExpert(
			@RequestParam(value = "file") MultipartFile file, Principal principal) throws FFmpegException {
		User u = userService.findOneUser(principal.getName());
		if (u == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		OriginalVideo originalVideo = originalVideoService.addOriginalVideoBasic(u, file,ConversionTypeBasic.WEB );
		videoTranscodingService.transcodeVideo(originalVideo);
		return new ResponseEntity<>(originalVideo, HttpStatus.CREATED);
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
