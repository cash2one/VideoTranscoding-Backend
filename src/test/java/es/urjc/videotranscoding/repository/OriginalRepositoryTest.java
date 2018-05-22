package es.urjc.videotranscoding.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Optional;

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

import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.entities.UserRoles;

/**
 * Integration test for original repository
 * 
 * @author luisca
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(locations = { "classpath:/xml/ffmpeg-config-test.xml" })
@EnableJpaRepositories
@SpringBootTest
public class OriginalRepositoryTest {
	private User u1;
	private Original original;
	@Autowired
	private OriginalRepository originalRepository;
	@Autowired
	private UserRepository userRepository;

	@Before
	public void beforeTest() {
		u1 = new User("patio@gmail.com", "admin", "pass", "", UserRoles.ADMIN, UserRoles.USER);
		userRepository.save(u1);
		original = new Original("originalVideo", "newPath", u1);
		originalRepository.save(original);
	}

	@After
	public void afterTest() {
		u1.removeVideo(original);
		userRepository.save(u1);
		originalRepository.delete(original);
		userRepository.delete(u1);
	}

	@Test
	public void repositoryCheckOriginal() {
		try {
			Optional<Original> originalFind = originalRepository.findById(original.getOriginalId());
			if (originalFind.isPresent()) {
				assertEquals(originalFind.get().getName(), original.getName());
				assertEquals(originalFind.get().getPath(), original.getPath());
			} else {
				fail("Not video found");
			}
		} catch (Exception e) {
			fail("Exception on search original");
		}
	}

	@Test
	public void repositoryDeleteOriginal() {
		try {
			Original original2 = new Original("New name2", "new pth 2", u1);
			originalRepository.save(original2);
			Optional<Original> originalFind2 = originalRepository.findById(original2.getOriginalId());
			if (originalFind2.isPresent()) {
				User userToDeletedOriginal = originalFind2.get().getUserVideo();
				userToDeletedOriginal.removeVideo(originalFind2.get());
				userRepository.save(userToDeletedOriginal);
				originalRepository.deleteById(originalFind2.get().getOriginalId());
			} else {
				fail("Not video found");
			}
		} catch (Exception e) {
			fail("Exception on delete original");
		}
	}

	@Test
	public void repositoryEditOriginal() {
		try {
			Optional<Original> originalFind = originalRepository.findById(original.getOriginalId());
			if (originalFind.isPresent()) {
				originalFind.get().setName("New name");
				originalFind.get().setPath("New path");
				originalRepository.save(originalFind.get());
			} else {
				fail("Not video found");
			}
		} catch (Exception e) {
			fail("Exception on edit original");
		}
	}

	@Test
	public void repositorySameOriginalSaved() {
		try {
			Original original2 = new Original("originalVideo", "newPath", u1);
			originalRepository.save(original2);
			originalRepository.findAll();
			fail("No throwed exception");
		} catch (Exception e) {

		}
	}

}
