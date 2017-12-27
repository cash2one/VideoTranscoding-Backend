package URJC.VideoTranscoding.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import URJC.VideoTranscoding.ffmpeg.TranscodingService;

@RestController
public class MainRestController{
	@Autowired
	private TranscodingService ffmpegTranscoding;

	@GetMapping(value = "/progress")
	public String getProgress(){
		return ffmpegTranscoding.getErrorGobbler().getProgress();
	}
}
