package urjc.videotranscoding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import urjc.videotranscoding.codecs.ConversionType;
import urjc.videotranscoding.entities.OriginalVideo;
import urjc.videotranscoding.entities.User;
import urjc.videotranscoding.entities.UserRoles;
import urjc.videotranscoding.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {
	@Autowired
	private UserRepository users;

	@Override
	public void run(String... strings) throws Exception {
		User u1 = new User("patio@gmail.com", "admin", "pass", "",
				UserRoles.ADMIN, UserRoles.USER);
		OriginalVideo video= new OriginalVideo("NUEVO VIDEO");
	
		video.setListAllConversions(ConversionType.MKV_H264360_COPY);
		u1.addVideo(video);
		users.save(u1);
		

	}
}
