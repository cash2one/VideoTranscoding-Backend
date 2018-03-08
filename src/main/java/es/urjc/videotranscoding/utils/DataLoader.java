package es.urjc.videotranscoding.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import es.urjc.videotranscoding.codecs.ConversionType;
import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.entities.UserRoles;
import es.urjc.videotranscoding.repository.OriginalRepository;
import es.urjc.videotranscoding.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {
	private final String VIDEO_DEMO = "path.video.demo";

	@Autowired
	private UserRepository users;
	@Autowired
	private OriginalRepository originalRepository;
	@Resource
	private Properties propertiesFFmpeg;

	public void run(String... strings) throws Exception {
		User u1 = new User("patio@gmail.com", "admin", "pass", "", UserRoles.ADMIN, UserRoles.USER);
		Original video = new Original("Nuevo Video", propertiesFFmpeg.getProperty(VIDEO_DEMO), u1);
		Conversion newVideo = new Conversion(ConversionType.MKV_H264360_COPY, video);
		Conversion newVideo2 = new Conversion(ConversionType.MKV_H264480_COPY, video);
		List<Conversion> lista = new ArrayList<>();
		lista.add(newVideo);
		lista.add(newVideo2);
		video.setAllConversions(lista);
		u1.addVideo(video);
		User user = users.findByEmail(u1.getEmail());
		if (user == null) {
			users.save(u1);
			originalRepository.save(video);
		}

	}
}
