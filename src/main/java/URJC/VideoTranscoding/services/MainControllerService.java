package URJC.VideoTranscoding.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MainControllerService{
	/**
	 * 
	 * @param file
	 * @param folderOutput
	 * @return
	 */
	public Path saveFile(MultipartFile file,String folderOutput){
		try{
			byte[] bytes = file.getBytes();
			Path path = Paths.get(folderOutput + file.getOriginalFilename().replace(" ","_"));
			Files.write(path,bytes);
			return path;
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
}
