package es.urjc.videotranscoding.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.videotranscoding.entities.OriginalVideo;
import es.urjc.videotranscoding.service.OriginalVideoService;
@RestController
@RequestMapping(value = "/api/originalVideo")
public class OriginalVideoRestController {

	@Autowired
	private OriginalVideoService originalVideoService;
	public interface Details
			extends
				OriginalVideo.Basic,
				OriginalVideo.Details {
	}

	@JsonView(Details.class)
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<OriginalVideo>> getVideos() {
		List<OriginalVideo> users = originalVideoService.findAllVideos();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

}
