package urjc.videotranscoding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import urjc.videotranscoding.entities.User;
import urjc.videotranscoding.entities.UserRoles;
import urjc.videotranscoding.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner{
	@Autowired
	private UserRepository users;

	@Override
	public void run(String...strings) throws Exception{
		User u1 = new User("patio@gmail.com","admin","pass","",UserRoles.ADMIN,UserRoles.USER);

		users.save(u1);
		
	}
}
