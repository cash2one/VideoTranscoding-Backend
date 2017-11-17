package URJC.VideoTranscoding.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import URJC.VideoTranscoding.codecs.ConversionType;
import URJC.VideoTranscoding.exception.FFmpegException;
import URJC.VideoTranscoding.ffmpeg.TranscodingService;
import URJC.VideoTranscoding.services.MainControllerService;

@Controller
public class MainController {
	private final String DEFAULT_UPLOAD_FILES = "path.folder.ouput";
	private final String FFMPEG_INSTALLATION_CENTOS7 = "path.ffmpeg.centos";
	private final String FFMPEG_INSTALLATION_MACOSX = "path.ffmpeg.macosx";
	@Autowired
	private TranscodingService ffmpegTranscoding;
	@Autowired
	private MainControllerService mainControllerService;
	@Resource
	private Properties propertiesFFmpeg;

	/**
	 * 
	 * @param m
	 * @return
	 */
	@GetMapping(value = "/")
	public String getIndex(Model m) {
		EnumSet<ConversionType> conversionType = EnumSet.allOf(ConversionType.class);
		m.addAttribute("conversionType", conversionType);
		return "index";
	}

	/**
	 * 
	 * @param file
	 * @param model
	 * @param conversionType
	 * @return
	 * @throws IOException
	 */
	@PostMapping(value = "/uploadFile")
	public String singleFileUpload(@RequestParam("fileupload") MultipartFile file, Model model, String conversionType)
			throws IOException {
		List<ConversionType> conversionTypes = new ArrayList<ConversionType>();
		// TODO ConvesionTYPE NOT NULL
		Arrays.stream(conversionType.split(",")).forEach(s -> conversionTypes.add(ConversionType.valueOf(s)));
		Path pathToReturn = mainControllerService.saveFile(file, propertiesFFmpeg.getProperty(DEFAULT_UPLOAD_FILES));
		String FFMPEG_PATH;
		if ((System.getProperty("os.name").equals("Mac OS X"))) {
			FFMPEG_PATH = propertiesFFmpeg.getProperty(FFMPEG_INSTALLATION_MACOSX);
		} else {
			FFMPEG_PATH = propertiesFFmpeg.getProperty(FFMPEG_INSTALLATION_CENTOS7);
		}
		Thread one = new Thread() {
			@Override
			public void run() {
				try {
					ffmpegTranscoding.transcode(new File((FFMPEG_PATH)), pathToReturn.toFile(),
							Paths.get(pathToReturn.getParent().toString()), conversionTypes);
				} catch (FFmpegException e) {
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
