package URJC.VideoTranscoding.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import URJC.VideoTranscoding.codecs.ConversionType;

@Controller
public class MainController{
	public static final String DEFAULT_IMG_FOLDER = "src/main/resources/static/";
	public static final String IMG_CONTROLLER_URL = "/uploadFile";
	public String fileInput = "/Users/luisca/Desktop";

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
	public String singleFileUpload(@RequestParam("fileupload") MultipartFile file,Model m){
		try{
			byte[] bytes = file.getBytes();
			Path path = Paths.get(DEFAULT_IMG_FOLDER + file.getOriginalFilename());
			System.out.println(path);
			Files.write(path,bytes);
			m.addAttribute("message","You successfully uploaded '" + file.getOriginalFilename() + "'");
		}catch(IOException e){
			e.printStackTrace();
		}
		return "fileUploaded";
	}
}
