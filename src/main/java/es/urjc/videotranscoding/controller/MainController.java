package es.urjc.videotranscoding.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

import es.urjc.videotranscoding.codecs.ConversionType;
import es.urjc.videotranscoding.core.VideoTranscodingService;
import es.urjc.videotranscoding.exception.FFmpegException;
import es.urjc.videotranscoding.service.FileService;

@Controller
public class MainController {

	@Autowired
	private VideoTranscodingService ffmpegTranscoding;
	//TODO UNUSED
	@SuppressWarnings("unused")
	@Autowired
	private FileService fileService;
	@Resource
	private Properties propertiesFFmpeg;

	/**
	 * @param m
	 * @return
	 */
	@GetMapping(value = "/")
	public String getIndex(Model m) {
		EnumSet<ConversionType> conversionType = EnumSet.allOf(ConversionType.class);
		m.addAttribute("conversionType", conversionType);
		return "index";
	}

	@GetMapping(value = "/up")
	public String getIndex2(Model m) {
		EnumSet<ConversionType> conversionType = EnumSet.allOf(ConversionType.class);
		m.addAttribute("conversionType", conversionType);
		return "upload";
	}

	@GetMapping(value = "/status")
	public String getStatus(Model m) {
		return "status";
	}

	/**
	 * @param file
	 * @param model
	 * @param conversionType
	 * @return
	 * @throws IOException
	 */
	@PostMapping(value = "/uploadFile")
	public String singleFileUpload(@RequestParam("fileupload") MultipartFile file, Model model, String conversionType) {
		List<ConversionType> conversionTypes = new ArrayList<ConversionType>();
		// TODO Conversion TYPE NOT NULL
		Arrays.stream(conversionType.split(",")).forEach(s -> conversionTypes.add(ConversionType.valueOf(s)));
		Thread one = new Thread() {
			@Override
			public void run() {
				try {
					// TODO Rehacer este controlador con fileService
					ffmpegTranscoding.transcodeVideo( null);
				} catch (FFmpegException e) {
					// TODO EXCEPTION
					e.printStackTrace();
				}
			}
		};
		one.start();
		model.addAttribute("message",
				"You successfully uploaded '" + file.getOriginalFilename() + "' and your file is being transcode");
		return "fileUploaded";
	}
}
