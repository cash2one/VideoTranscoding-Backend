package URJC.VideoTranscoding.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadFile {
	public static final String DEFAULT_IMG_FOLDER = "src/main/resources/static/";
	public static final String IMG_CONTROLLER_URL = "/uploadFile";
	public String fileInput = "/Users/luisca/Desktop";

	@PostMapping("/upload")
	public String singleFileUpload(@RequestParam("fileupload") MultipartFile file, Model m) {

		if (file.isEmpty()) {
			m.addAttribute("message", "Please select a file to upload");
			return "redirect:/uploadFile";
		}
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(DEFAULT_IMG_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);

			m.addAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/uploadFile";
	}

}
