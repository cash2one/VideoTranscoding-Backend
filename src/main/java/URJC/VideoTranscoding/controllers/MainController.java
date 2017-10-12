package URJC.VideoTranscoding.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	public static final String fileInput="/Users/luisca/Desktop";

	@RequestMapping(value = "/")
	public String getIndex() {
		return "index";
	}

	@RequestMapping(value = "up")
	public String getUploadFile() {
		return "uploadFile";
	}

//	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
//	public @ResponseBody String uploadFileHandler(@RequestParam("name") String name,
//			@RequestParam("file") MultipartFile file) {
//
//		if (!file.isEmpty()) {
//			try {
//				byte[] bytes = file.getBytes();
//
//				// Creating the directory to store file
//				File dir = new File(fileInput + File.separator + "tmpFiles");
//				if (!dir.exists())
//					dir.mkdirs();
//
//				// Create the file on server
//				File serverFile = new File(fileInput+File.separator + name);
//				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
//				stream.write(bytes);
//				stream.close();
//
//				return "You successfully uploaded file=" + name;
//			} catch (Exception e) {
//				return "You failed to upload " + name + " => " + e.getMessage();
//			}
//		} else {
//			return "You failed to upload " + name + " because the file was empty.";
//		}
//	}
	
	
}
