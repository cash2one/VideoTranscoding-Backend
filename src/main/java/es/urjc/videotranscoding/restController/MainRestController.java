package es.urjc.videotranscoding.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import es.urjc.videotranscoding.core.VideoTranscodingService;
import io.swagger.annotations.Api;

@RestController
@Api(tags = "Main Api Operations")
public class MainRestController {
	@Autowired
	private VideoTranscodingService ffmpegTranscoding;

	@GetMapping(value = "/ajaxCall")
	public String getStatusAjax() {
		try {
			String progress = ffmpegTranscoding.getErrorGobbler().getProgress().replace(",", ".");
			return progress;
		} catch (NullPointerException e) {
			return "Vacio";
		}
	}
}
