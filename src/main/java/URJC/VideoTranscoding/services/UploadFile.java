package URJC.VideoTranscoding.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadFile {
	public static final String DEFAULT_IMG_FOLDER = "src/main/resources/static/";
	public static final String IMG_CONTROLLER_URL = "/uploadFile";

	@PostMapping("/uploadFile")
	public String singleFileUpload(@RequestParam("file") MultipartFile file, Model redirectAttributes) {

		if (file.isEmpty()) {
			redirectAttributes.addAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus/false";
		}
		try {

			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			Path path = Paths.get(DEFAULT_IMG_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);

			redirectAttributes.addAttribute("message",
					"You successfully uploaded '" + file.getOriginalFilename() + "'");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/uploadStatus/true";
	}

	@GetMapping("/uploadStatus/{var}")
	public String uploadStatus(@PathVariable String var, Model m) {
		if (Boolean.parseBoolean(var) == false) {
			m.addAttribute("message", "Todo mal");
			return "uploadStatus";
		} else {
			m.addAttribute("message", "Todo bien");
			return "uploadStatus";
		}
	}

}
