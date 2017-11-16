package URJC.VideoTranscoding.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import URJC.VideoTranscoding.codecs.ConversionType;
import URJC.VideoTranscoding.exception.FFmpegException;
import URJC.VideoTranscoding.service.MainControllerService;
import URJC.VideoTranscoding.service.TranscodingService;

@Controller
public class MainController{
	public static final String DEFAULT_UPLOAD_FILES = "/Users/luisca/Documents/";
	private final String FFMPEG_INSTALLATION_CENTOS7 = "/usr/bin/ffmpeg";
	private final String FFMPEG_INSTALLATION_MACOSX = "/usr/local/Cellar/ffmpeg/3.4/bin/ffmpeg";
	public String fileInput = "/Users/luisca/Desktop";
	@Autowired
	private TranscodingService ffmpegTranscoding;
	@Autowired
	private MainControllerService mainControllerService;

	@GetMapping(value = "/")
	public String getIndex(){
		return "index";
	}

	@GetMapping(value = "/upload")
	public String getUploadFile(Model m){
		EnumSet<ConversionType> conversionType = EnumSet.allOf(ConversionType.class);
		m.addAttribute("conversionType",conversionType);
		return "uploadFile";
	}

	@PostMapping(value = "/uploadFile")
	public String singleFileUpload(@RequestParam("fileupload") MultipartFile file,Model m,String conversionType)
				throws IOException{
		List<ConversionType> conversionTypes = new ArrayList<ConversionType>();
		Arrays.stream(conversionType.split(",")).forEach(s -> conversionTypes.add(ConversionType.valueOf(s)));
		Path pathToReturn = mainControllerService.saveFile(file,DEFAULT_UPLOAD_FILES);
		String FFMPEG_PATH;
		if((System.getProperty("os.name").equals("Mac OS X"))){
			FFMPEG_PATH = FFMPEG_INSTALLATION_MACOSX;
		}else{
			FFMPEG_PATH = FFMPEG_INSTALLATION_CENTOS7;
		}
		Thread one = new Thread(){
			@Override
			public void run(){
				try{
					ffmpegTranscoding.transcode(new File((FFMPEG_PATH)),pathToReturn.toFile(),
								Paths.get(pathToReturn.getParent().toString()),conversionTypes);
				}catch(FFmpegException e){
					e.printStackTrace();
				}
			}
		};
		one.start();
		m.addAttribute("message",
					"You successfully uploaded '" + file.getOriginalFilename() + "' and your file is being processed");
		return "fileUploaded";
	}
}
