package es.urjc.videotranscoding.restController;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.exception.ExceptionForRest;
import es.urjc.videotranscoding.exception.FFmpegException;
import es.urjc.videotranscoding.service.ConversionService;
import es.urjc.videotranscoding.service.OriginalService;
import es.urjc.videotranscoding.utils.FileDownloader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/downloader")
@Api(tags = "Downloads Api Operations")
public class DownloaderRestController {
	@Autowired
	private OriginalService originalService;
	@Autowired
	private ConversionService conversionService;

	/**
	 * @param response
	 * @param id
	 * @return
	 * @throws FFmpegException
	 * @throws IOException
	 */
	@ApiOperation(value = "Download the Original or transcode Video")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> downloadDirectFilm(HttpServletResponse response, @PathVariable long id)
			throws FFmpegException {
		Optional<Original> video = originalService.findOneVideo(id);
		if (!video.isPresent()) {
			return new ResponseEntity<>(getDownloadConversion(id, response), HttpStatus.OK);
		} else {
			File filepath = new File(video.get().getPath());
			FileDownloader.fromFile(filepath).with(response).serveResource();
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	/**
	 * 
	 * @param id
	 * @param response
	 * @return
	 * @throws FFmpegException
	 */
	private Conversion getDownloadConversion(long id, HttpServletResponse response) throws FFmpegException {
		Optional<Conversion> video = conversionService.findOneConversion(id);
		Conversion conversion = video.get();
		if (conversion == null) {
			// TODO
			throw new FFmpegException(FFmpegException.EX_FFMPEG_EMPTY_OR_NULL);
		}
		File filepath = new File(video.get().getPath());
		FileDownloader.fromFile(filepath).with(response).serveResource();
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
