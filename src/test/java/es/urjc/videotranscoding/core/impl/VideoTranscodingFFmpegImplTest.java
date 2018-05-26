package es.urjc.videotranscoding.core.impl;

import static org.assertj.core.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.urjc.videotranscoding.codecs.ConversionType;
import es.urjc.videotranscoding.codecs.ConversionTypeBasic;
import es.urjc.videotranscoding.core.VideoTranscodingService;
import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.entities.UserRoles;
import es.urjc.videotranscoding.exception.FFmpegException;
import es.urjc.videotranscoding.exception.FFmpegRuntimeException;
import es.urjc.videotranscoding.service.OriginalService;
import es.urjc.videotranscoding.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/xml/ffmpeg-config-test.xml" })
@EnableJpaRepositories
public class VideoTranscodingFFmpegImplTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	private final String VIDEO_DEMO = "path.video.demo";
	private final static String FOLDER_OUPUT_ORIGINAL = "path.folder.original";
	private final static String FOLDER_OUTPUT_TRANSCODE = "path.folder.ouput";

	@Autowired
	private VideoTranscodingService transcoding;
	@Autowired
	private OriginalService originalService;
	@Autowired
	private UserService userService;
	@Resource
	private Properties propertiesFFmpegTest;
	@Resource
	private Properties propertiesFFmpeg;
	@Resource
	private Properties propertiesFicheroCore;
	private static User u1;

	@BeforeClass
	public static void beforeClass() {
	}

	@Before
	public void setUp() throws IOException {
		createFolder(propertiesFFmpeg.getProperty(FOLDER_OUPUT_ORIGINAL));
		createFolder(propertiesFFmpeg.getProperty(FOLDER_OUTPUT_TRANSCODE));

	}

	@After
	public void setDown() throws IOException {
		userService.deleteUser(u1);
	}

	private void createFolder(String string) {
		File folder = new File(string);
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}

	@AfterClass
	public static void afterClass() {
		File tempFolderOuput = new File("/temp");
		tempFolderOuput.delete();
	}

	@Test
	@Ignore
	public void transcodeSucess() {
		u1 = new User("patio@gmail.com", "admin", "pass", "", UserRoles.ADMIN, UserRoles.USER);
		Original video = new Original("Perico", "/src/main/resources/resources/big_buck_bunny.mp4", u1);
		Conversion newVideo = new Conversion(ConversionType.MKV_H264360_COPY, video);
		List<Conversion> lista = new ArrayList<>();
		lista.add(newVideo);
		video.setAllConversions(lista);
		u1.addVideo(video);
		userService.save(u1);
		originalService.save(video);
		try {
			transcoding.transcodeVideo(video);
			Thread.sleep(1000000000);
		} catch (FFmpegException e) {
			e.printStackTrace();
			fail("No should fail");
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("No should fail");
		} catch (FFmpegRuntimeException e) {
			e.printStackTrace();
			fail("No should fail");
		}
	}

	@Test
	@Ignore
	public void allTypeTranscode() throws InterruptedException {
		u1 = new User("patio@gmail.com", "admin", "pass", "", UserRoles.ADMIN, UserRoles.USER);
		Original video = new Original("Perico", "/tmp/VideoTranscoding/videos/original/video.mp4", u1);

		for (ConversionType iterable_element : EnumSet.allOf(ConversionType.class)) {
			List<Conversion> lista = new ArrayList<>();
			lista.add(new Conversion(iterable_element, video));
			video.setAllConversions(lista);
			u1.addVideo(video);
			userService.save(u1);
			originalService.save(video);
			try {
				transcoding.transcodeVideo(video);
				Thread.sleep(1000000000);
			} catch (FFmpegException e) {
				e.printStackTrace();
			}
		}

	}

	@Test
	@Ignore
	public void transcodeTypeBasicMovil() {
		User u1 = new User("patio@gmail.com", "admin", "pass", "", UserRoles.ADMIN, UserRoles.USER);
		Original video = new Original("Perico", propertiesFFmpegTest.getProperty(VIDEO_DEMO), u1);
		List<Conversion> lista = new ArrayList<>();
		List<ConversionType> x = ConversionTypeBasic.getConversion(ConversionTypeBasic.Types.MOVIL);
		x.forEach(c -> {
			lista.add(new Conversion(c, video));
		});
		video.setAllConversions(lista);
		u1.addVideo(video);
		userService.save(u1);
		originalService.save(video);
		try {
			transcoding.transcodeVideo(video);
			Thread.sleep(10000);
		} catch (FFmpegException e) {
			e.printStackTrace();
			fail("No should fail");
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("No should fail");
		} catch (FFmpegRuntimeException e) {
			e.printStackTrace();
			fail("No should fail");
		}
	}
}
