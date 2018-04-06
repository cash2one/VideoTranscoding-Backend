package es.urjc.videotranscoding.restController;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/downloader")
@Api(tags = "Downloads Api Operations")
@CrossOrigin(origins = "*")
public class DownloaderRestController {
	private static final String TRACE_IO_EXCEPTION_GENERAL = "ffmpeg.ioException.general";
	protected final Logger logger = Logger.getLogger(DownloaderRestController.class);
	@Autowired
	private OriginalService originalService;
	@Autowired
	private ConversionService conversionService;

	/**
	 * @param response
	 * @param id
	 * @return
	 * @throws FFmpegException
	 * 
	 * @throws IOException
	 */
	@ApiOperation(value = "Download the Original or transcode Video")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> downloadDirectFilm(HttpServletResponse response, @PathVariable long id)
			throws FFmpegException {
		Optional<Original> video = originalService.findOneVideo(id);
		if (!video.isPresent()) {
			Optional<Conversion> conversion = conversionService.findOneConversion(id);
			if (conversion.isPresent()) {
				Conversion conversionVideo = conversion.get();
				File filepath = new File(conversionVideo.getPath());
				String mimeType = URLConnection.guessContentTypeFromName(filepath.getName());
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}
				response.setContentType(mimeType);
				response.setHeader("Content-Disposition",
						String.format("inline; filename=\"" + filepath.getName() + "\""));
				response.setHeader("Content-Length", String.valueOf(filepath.length()));
				try {
					InputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
					FileCopyUtils.copy(inputStream, response.getOutputStream());
					return new ResponseEntity<>(HttpStatus.OK);
				} catch (IOException e) {
					logger.l7dlog(Level.ERROR, TRACE_IO_EXCEPTION_GENERAL, e);
					throw new FFmpegException(FFmpegException.EX_IO_EXCEPTION_GENERAL);
				}
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} else {
			File filepath = new File(video.get().getPath());
			String mimeType = URLConnection.guessContentTypeFromName(filepath.getName());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}
			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + filepath.getName() + "\""));
			response.setHeader("Content-Length", String.valueOf(filepath.length()));
			try {
				InputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
				FileCopyUtils.copy(inputStream, response.getOutputStream());
				return new ResponseEntity<>(HttpStatus.OK);
			} catch (IOException e) {
				logger.l7dlog(Level.ERROR, TRACE_IO_EXCEPTION_GENERAL, e);
				throw new FFmpegException(FFmpegException.EX_IO_EXCEPTION_GENERAL);
			}
		}
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
