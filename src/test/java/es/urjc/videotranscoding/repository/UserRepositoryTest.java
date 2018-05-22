package es.urjc.videotranscoding.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.entities.UserRoles;

/**
 * Integration test for user repository
 * 
 * @author luisca
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(locations = { "classpath:/xml/ffmpeg-config-test.xml" })
@EnableJpaRepositories
@SpringBootTest
public class UserRepositoryTest {
	private User u1;

	@Autowired
	private UserRepository userRepository;

	@Before
	public void beforeTest() {
		u1 = new User("patio@gmail.com", "admin", "pass", "", UserRoles.ADMIN, UserRoles.USER);
		userRepository.save(u1);
	}

	@After
	public void afterTest() {
		userRepository.delete(u1);
	}

	@Test
	public void repositoryCheckUser() {
		User userResult = userRepository.findByEmail(u1.getEmail());
		assertEquals(userResult.getEmail(), u1.getEmail());
		assertEquals(userResult.getNick(), u1.getNick());
	}

	@Test
	public void repositoryDeleteUser() {
		try {
			User u2 = new User("patio@gmail.com", "admin", "pass", "", UserRoles.ADMIN, UserRoles.USER);
			userRepository.save(u2);
			userRepository.delete(u2);
		} catch (Exception e) {
			fail("Exception on delete user");
		}
	}

	@Test
	public void repositoryEditUser() {
		try {
			u1.setEmail("email@gmail.com");
			u1.setNick("nick");
			User editedUser = userRepository.save(u1);
			assertEquals(editedUser.getEmail(), u1.getEmail());
			assertEquals(editedUser.getNick(), u1.getNick());
		} catch (Exception e) {
			fail("Exception");
		}
	}

	@Test
	public void repositorySameUserSaved() {
		try {
			User u2 = new User("patio@gmail.com", "admin", "pass", "", UserRoles.ADMIN, UserRoles.USER);
			userRepository.save(u2);
			userRepository.findAll();
			fail("No throwed exception");
		} catch (Exception e) {
		}

	}

}
