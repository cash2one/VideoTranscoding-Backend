package urjc.videotranscoding.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import urjc.videotranscoding.service.FFmpegService;

@RestController
@RequestMapping(value = "/api")
@Api(tags = "FFmpeg RestController")
public class FFmpegRestController{
	@SuppressWarnings("unused")
	@Autowired
	private FFmpegService ffmpegService;

	// TODO API OPERATIONS
	@ApiOperation(value = "Devuelve las conversiones que tiene")
	@GetMapping(value = "/")
	public void getIndex(){
	}
}
