package es.urjc.videotranscoding.service.impl;

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
public class VideoTranscodingFFmpegTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	private final String VIDEO_DEMO = "path.video.demo";
	@Autowired
	private VideoTranscodingService transcoding;
	@Autowired
	private OriginalService originalService;
	@Autowired
	private UserService userService;
	@Resource
	private Properties propertiesFFmpegTest;
	@Resource
	private Properties propertiesFicheroCore;
	private static User u1;
	@BeforeClass
	public static void beforeClass() {
	}

	@Before
	public void setUp() throws IOException {
		createFolder("/tmp/videos/transcoded/");
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

	// @Test
	// public void ffmpegPathIsNull() {
	// try {
	// transcoding.transcodeVideo(null, null, null);
	// fail("No should fail for null ffmpeg file");
	// } catch (FFmpegException e) {
	// assertEquals(FFmpegException.EX_FFMPEG_EMPTY_OR_NULL, e.getCodigo());
	// }
	// }
	//
	// @Test
	// public void transcodeFailOnFakeFFMPEGFile() {
	// try {
	// transcoding.transcodeVideo("FAKE", null, null);
	// fail("No should fail for fake ffmpeg file");
	// } catch (FFmpegException e) {
	// assertEquals(FFmpegException.EX_FFMPEG_NOT_FOUND, e.getCodigo());
	// }
	// }
	//
	// @Test
	// public void transcodeFailOnNullFolderPath() {
	// try {
	// transcoding.transcodeVideo(FFMPEG_PATH, null, null);
	// fail("No should fail for fake input file");
	// } catch (FFmpegException e) {
	// assertEquals(FFmpegException.EX_FOLDER_OUTPUT_EMPTY_OR_NULL, e.getCodigo());
	// }
	// }
	//
	// @Test
	// public void transcodeFailOnFakeFolderOuput() {
	// try {
	// transcoding.transcodeVideo(FFMPEG_PATH, "FAKE", null);
	// fail("No should fail for fake folder output");
	// } catch (FFmpegException e) {
	// assertEquals(FFmpegException.EX_FOLDER_OUTPUT_NOT_EXITS, e.getCodigo());
	// }
	// }
	//
	// @Test
	// public void transcodeFailOnNullFolderOuput() {
	// try {
	// transcoding.transcodeVideo(FFMPEG_PATH, null, null);
	// fail("No should fail for null folder ouput");
	// } catch (FFmpegException e) {
	// assertEquals(FFmpegException.EX_FOLDER_OUTPUT_EMPTY_OR_NULL, e.getCodigo());
	// }
	// }
	//
	// @Test
	// public void transcodeFailOnNullParams() {
	// try {
	// transcoding.transcodeVideo(FFMPEG_PATH, FOLDER_OUTPUT_REAL.toString(), null);
	// fail("No should fail for null params");
	// } catch (FFmpegException e) {
	// assertEquals(FFmpegException.EX_ORIGINAL_VIDEO_NULL, e.getCodigo());
	// }
	// }
	//
	// @Test
	// public void transcodeFailOnEmptyParams() {
	// try {
	// transcoding.transcodeVideo(FFMPEG_PATH, FOLDER_OUTPUT_REAL.toString(), new
	// OriginalVideo("", "", null));
	// fail("No should empty params");
	// } catch (FFmpegException e) {
	// assertEquals(FFmpegException.EX_ORIGINAL_VIDEO_NOT_IS_SAVE, e.getCodigo());
	// }
	// }

	@Test
	public void transcodeSucess() {
		u1 = new User("patio@gmail.com", "admin", "pass", "", UserRoles.ADMIN, UserRoles.USER);
		Original video = new Original("Perico", propertiesFFmpegTest.getProperty(VIDEO_DEMO), u1);
		Conversion newVideo = new Conversion(ConversionType.MKV_H264360_COPY, video);
		Conversion newVideo2 = new Conversion(ConversionType.MKV_H264480_COPY, video);
		List<Conversion> lista = new ArrayList<>();
		lista.add(newVideo);
		lista.add(newVideo2);
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

	@Test
	public void allTypeTranscode() {
		u1 = new User("patio@gmail.com", "admin", "pass", "", UserRoles.ADMIN, UserRoles.USER);
		Original video = new Original("Perico", propertiesFFmpegTest.getProperty(VIDEO_DEMO), u1);
		List<Conversion> lista = new ArrayList<>();
		EnumSet.allOf(ConversionType.class).forEach(c -> {
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
		}
	}

	@Ignore
	@Test
	public void transcodeTypeBasicMovil() {
		User u1 = new User("patio@gmail.com", "admin", "pass", "", UserRoles.ADMIN, UserRoles.USER);
		Original video = new Original("Perico", propertiesFFmpegTest.getProperty(VIDEO_DEMO), u1);
		List<Conversion> lista = new ArrayList<>();
		List<ConversionType> x = ConversionTypeBasic.MOVIL;
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
