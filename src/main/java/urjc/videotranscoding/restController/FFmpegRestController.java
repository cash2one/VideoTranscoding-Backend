package urjc.videotranscoding.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import urjc.videotranscoding.service.FFmpegService;


@RestController
@RequestMapping(value = "/api")
public class FFmpegRestController{
	@SuppressWarnings("unused")
	@Autowired
	private FFmpegService ffmpegService;

	@GetMapping(value = "/")
	public void getIndex(){
	}
}
