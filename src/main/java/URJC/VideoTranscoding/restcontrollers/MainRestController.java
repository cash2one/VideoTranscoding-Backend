package urjc.VideoTranscoding.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import urjc.VideoTranscoding.ffmpeg.TranscodingService;

@RestController
public class MainRestController {
	@Autowired
	private TranscodingService ffmpegTranscoding;

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
