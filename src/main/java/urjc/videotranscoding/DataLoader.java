package urjc.videotranscoding;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import urjc.videotranscoding.codecs.ConversionType;
import urjc.videotranscoding.entities.ConversionVideo;
import urjc.videotranscoding.entities.OriginalVideo;
import urjc.videotranscoding.entities.User;
import urjc.videotranscoding.entities.UserRoles;
import urjc.videotranscoding.repository.ConversionVideoRepository;
import urjc.videotranscoding.repository.OriginalVideoRepository;
import urjc.videotranscoding.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {
	@Autowired
	private UserRepository users;
	@Autowired
	private OriginalVideoRepository originalVideoRepository;
	@Autowired
	private ConversionVideoRepository conversionVideoRepository;

	@Override
	public void run(String... strings) throws Exception {
		User u1 = new User("patio@gmail.com", "admin", "pass", "",
				UserRoles.ADMIN, UserRoles.USER);
		OriginalVideo video = new OriginalVideo("NUEVO VIDEO", Boolean.FALSE);
		//
		// List<ConversionType> conversions = new ArrayList<>();
		// conversions.add(ConversionType.MKV_H264360_COPY);
		// conversions.add(ConversionType.MKV_H2641080_COPY);
		ConversionVideo newVideo = new ConversionVideo("adwadawd",
				ConversionType.MKV_H264360_COPY,video);
		ConversionVideo newVideo2 = new ConversionVideo("adwadawadawdd",
				ConversionType.MKV_H2641080_COPY,video);
		List<ConversionVideo> lista = new ArrayList<>();
		lista.add(newVideo);
		lista.add(newVideo2);
		video.setAllConversions(lista);
		u1.addVideo(video);
		users.save(u1);
		conversionVideoRepository.save(newVideo);
		conversionVideoRepository.save(newVideo2);
		originalVideoRepository.save(video);

		
		

	}
}
