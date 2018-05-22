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

import es.urjc.videotranscoding.codecs.ConversionType;
import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.entities.UserRoles;

/**
 * Integration test for conversion repository
 * 
 * @author luisca
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(locations = { "classpath:/xml/ffmpeg-config-test.xml" })
@EnableJpaRepositories
@SpringBootTest
public class ConversionRepositoryTest {
	private User u1;
	private Original original;
	private Conversion conversion;
	@Autowired
	private OriginalRepository originalRepository;
	@Autowired
	private ConversionRepository conversionRepository;
	@Autowired
	private UserRepository userRepository;

	@Before
	public void beforeTest() {
		u1 = new User("patio@gmail.com", "admin", "pass", "", UserRoles.ADMIN, UserRoles.USER);
		userRepository.save(u1);
		original = new Original("originalVideo", "newPath", u1);
		originalRepository.save(original);
		conversion = new Conversion(ConversionType.MKV_H264360_AAC, original);
		conversionRepository.save(conversion);
	}

	@After
	public void afterTest() {
		original.removeConversion(conversion);
		originalRepository.save(original);
		u1.removeVideo(original);
		userRepository.save(u1);
		originalRepository.delete(original);
		userRepository.delete(u1);
	}

	@Test
	public void repositoryCheckConversion() {
		try {
			Optional<Conversion> conversionF = conversionRepository.findById(conversion.getConversionId());
			if (conversionF.isPresent()) {
				assertEquals(conversionF.get().getName(), conversion.getName());
				assertEquals(conversionF.get().getPath(), conversion.getPath());
			} else {
				fail("Not conversion found");
			}
		} catch (Exception e) {
			fail("Exception on search conversion");
		}
	}

	@Test
	public void repositoryEditConversion() {
		try {
			Optional<Conversion> conversionF = conversionRepository.findById(conversion.getConversionId());
			if (conversionF.isPresent()) {
				conversionF.get().setConversionType(ConversionType.MKV_H264480_COPY);
				conversionF.get().setFinished(true);
				conversionF.get().setPath("New path edit");
				conversionRepository.save(conversionF.get());
			} else {
				fail("Not conversion found");
			}
		} catch (Exception e) {
			fail("Exception on search conversion");
		}
	}

	@Test
	public void repositoryDeleteConversion() {
		try {
			Original original2 = new Original("New name2", "new pth 2", u1);
			originalRepository.save(original2);
			Conversion conversionNew = new Conversion(ConversionType.MKV_HEVC360_AAC, original2);
			conversionRepository.save(conversionNew);
			Optional<Conversion> conversionFind2 = conversionRepository.findById(conversionNew.getConversionId());
			if (conversionFind2.isPresent()) {
				Original originalT = conversionFind2.get().getParent();
				originalT.removeConversion(conversionFind2.get());
				conversionRepository.delete(conversionFind2.get());
				originalRepository.save(originalT);
			} else {
				fail("Not video found");
			}
		} catch (Exception e) {
			fail("Exception on delete original");
		}
	}

	/**
	 * The conversion is overwrited
	 */
	@Test
	public void repositorySameConversionSaved() {
		try {
			Conversion conversion2 = new Conversion(ConversionType.MKV_H264360_AAC, original);
			conversionRepository.save(conversion2);
			conversionRepository.findAll();
		} catch (Exception e) {
			fail("No exception");
		}
	}

}
