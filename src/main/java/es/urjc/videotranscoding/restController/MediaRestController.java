package es.urjc.videotranscoding.restController;

import java.io.File;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import es.urjc.videotranscoding.utils.FileDownloader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/media")
@Api(tags = "Media Api Operations")
@CrossOrigin(origins = "*")
public class MediaRestController {
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
	public ResponseEntity<List<Original>> getAllVideoConversions(Principal principal, Pageable pageable) {
		if (principal == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		User u = userService.findOneUser(principal.getName());
		Page<Original> allOriginalVideos = originalService.findAllByPageAndUser(pageable, u);
		if (allOriginalVideos == null || allOriginalVideos.getContent().size() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Original>>(allOriginalVideos.getContent(), HttpStatus.OK);

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
	public ResponseEntity<?> getOriginalVideo(@PathVariable long id, Principal principal) throws FFmpegException {
		if (principal == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		User u = userService.findOneUser(principal.getName());
		Object video = originalService.findOneVideo(id, u);
		if (video == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		} else {
			return new ResponseEntity<>(video, HttpStatus.OK);
		}
	}

	@JsonView(Details.class)
	@ApiOperation(value = "Delete all Videos for all the Users, Only Admin")
	@DeleteMapping(value = "/all")
	public ResponseEntity<?> deleteAllVideosByAdmin(Principal principal) {
		User u = userService.findOneUser(principal.getName());
		if (u == null || !u.isAdmin()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		originalService.deleteAllVideosByAdmin();
		return new ResponseEntity<>(u, HttpStatus.OK);
	}

	@JsonView(Details.class)
	@ApiOperation(value = "Delete Original or conversion with the id")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteVideos(Principal principal, @PathVariable long id) {
		if (principal == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		User u = userService.findOneUser(principal.getName());
		Optional<Original> video = originalService.findOneVideoWithoutSecurity(id);
		if (!video.isPresent()) {
			Optional<Conversion> conversion = conversionService.findOneConversion(id);
			if (conversion.isPresent()) {
				Conversion conversionVideo = conversion.get();
				conversionService.deleteConversion(conversionVideo.getParent(), conversionVideo, u);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} else {
			originalService.deleteOriginal(video.get(), u);
			return new ResponseEntity<Original>(HttpStatus.OK);
		}
	}

	@JsonView(Details.class)
	@ApiOperation(value = "Delete all the videos of the user")
	@DeleteMapping(value = "/")
	public ResponseEntity<?> deleteAllMyVideos(Principal principal) {
		User u = userService.findOneUser(principal.getName());
		if (u == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		User userToReturn = originalService.deleteAllVideos(u);
		return new ResponseEntity<User>(userToReturn, HttpStatus.OK);

	}

	/**
	 * Search a OriginalVideo or Conversion and get it on Download
	 * 
	 * @param response
	 *            for the servlet
	 * @param id
	 *            for search the video
	 * @return the download on servletResponse
	 * @throws FFmpegException
	 */
	@ApiOperation(value = "Download the Original or transcode Video")
	@GetMapping(value = "/{id}/content")
	public ResponseEntity<?> downloadDirectFilm(@RequestParam(value = "forceSave") String forceSave,
			HttpServletResponse response, @PathVariable long id) throws FFmpegException {
		if (forceSave != null) {

		} else {
		}
		Optional<Original> video = originalService.findOneVideoWithoutSecurity(id);
		if (!video.isPresent()) {
			Optional<Conversion> conversion = conversionService.findOneConversion(id);
			if (conversion.isPresent()) {
				Conversion conversionVideo = conversion.get();
				File filepath = new File(conversionVideo.getPath());
				FileDownloader.fromFile(filepath).with(response).serveResource();
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} else {
			File filepath = new File(video.get().getPath());
			FileDownloader.fromFile(filepath).with(response).serveResource();
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	/**
	 * With this method you can see the video transcoded or the original
	 *
	 * @param response
	 *            for the video
	 * @param request
	 *            for the video
	 * @param id
	 *            of the original Video or Transcoded
	 * @return
	 */
//	@ApiOperation(value = "Watch the Original or transcode Video")
//	@GetMapping(value = "/{id}/content")
//	public ResponseEntity<?> watchVideo(HttpServletResponse response, HttpServletRequest request,
//			@PathVariable long id) {
//		Optional<Original> video = originalService.findOneVideoWithoutSecurity(id);
//		if (!video.isPresent()) {
//			Optional<Conversion> conversion = conversionService.findOneConversion(id);
//			if (conversion.isPresent()) {
//				Conversion conversionVideo = conversion.get();
//				Path p1 = Paths.get(conversionVideo.getPath());
//				FileWatcher.fromPath(p1).with(request).with(response).serveResource();
//				return new ResponseEntity<>(HttpStatus.OK);
//			} else {
//				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//			}
//		} else {
//			Path p1 = Paths.get(video.get().getPath());
//			FileWatcher.fromPath(p1).with(request).with(response).serveResource();
//			return new ResponseEntity<Original>(HttpStatus.OK);
//		}
//	}

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
