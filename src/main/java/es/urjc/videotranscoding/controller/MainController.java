package es.urjc.videotranscoding.controller;

import java.security.Principal;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.codecs.ConversionTypeBasic;
import es.urjc.videotranscoding.core.VideoTranscodingService;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.exception.FFmpegException;
import es.urjc.videotranscoding.service.OriginalService;
import es.urjc.videotranscoding.service.UserService;

@Controller
public class MainController {
	// TODO JAVADOC

	@Autowired
	private VideoTranscodingService videoTranscodingService;
	@Autowired
	private UserService userService;
	@Autowired
	private OriginalService originalService;

	@Resource
	private Properties propertiesFFmpeg;

	@GetMapping(value = "/")
	public String getIndex(Model m) {
		List<String> typeConversionBasic = ConversionTypeBasic.getAllTypesBasic();
		m.addAttribute("conversionType", typeConversionBasic);
		return "index";
	}

	@GetMapping(value = "/up")
	public String getIndex2(Model m) {
		EnumSet<ConversionTypeBasic.Types> conversionType = EnumSet.allOf(ConversionTypeBasic.Types.class);
		m.addAttribute("conversionType", conversionType);
		return "upload";
	}

	@GetMapping(value = "/status")
	public String getStatus(Model m) {
		return "status";
	}

	@PostMapping(value = "/uploadFile")
	public String singleFileUpload(@RequestParam("fileupload") MultipartFile file, Model model,
			@RequestParam(value = "conversionType") List<String> params, Principal principal) throws FFmpegException {

		User u = userService.findOneUser(principal.getName());
		if (u == null) {
			return "403";
		}
		Original original = originalService.addOriginalBasic(u, file, params);
		videoTranscodingService.transcodeVideo(original);
		model.addAttribute("message",
				"You successfully uploaded '" + file.getOriginalFilename() + "' and your file is being transcode");
		return "fileUploaded";
	}
}
