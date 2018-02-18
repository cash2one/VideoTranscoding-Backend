package urjc.videotranscoding.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

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

import urjc.videotranscoding.codecs.ConversionType;
import urjc.videotranscoding.core.VideoTranscodingService;
import urjc.videotranscoding.entities.ConversionVideo;
import urjc.videotranscoding.entities.OriginalVideo;
import urjc.videotranscoding.entities.User;
import urjc.videotranscoding.entities.UserRoles;
import urjc.videotranscoding.exception.FFmpegException;
import urjc.videotranscoding.service.OriginalVideoService;
import urjc.videotranscoding.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/xml/ffmpeg-config-test.xml" })
@EnableJpaRepositories
public class VideoTranscodingFFmpegTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	private final String FFMPEG_INSTALLATION_CENTOS7 = "path.ffmpeg.centos";
	private final String FFMPEG_INSTALLATION_MACOSX = "path.ffmpeg.macosx";
	private final String VIDEO_DEMO = "path.video.demo";
	private final String OS_MAC = "Mac OS X";
	private static File FFMPEG_PATH;
	private File FOLDER_OUTPUT_REAL;
	@Autowired
	private VideoTranscodingService transcoding;
	@Autowired
	private OriginalVideoService originalVideoService;
	@Autowired
	private UserService userService;
	@Resource
	private Properties propertiesFFmpegTest;
	@Resource
	private Properties propertiesFicheroCore;

	@BeforeClass
	public static void beforeClass() {
	}

	@Before
	public void setUp() throws IOException {
		if (System.getProperty("os.name").equals(OS_MAC)) {
			FFMPEG_PATH = new File(propertiesFFmpegTest.getProperty(FFMPEG_INSTALLATION_MACOSX));
		} else {
			FFMPEG_PATH = new File(propertiesFFmpegTest.getProperty(FFMPEG_INSTALLATION_CENTOS7));
		}
		FOLDER_OUTPUT_REAL = folder.newFolder("temp");
	}

	@AfterClass
	public static void afterClass() {
		File tempFolderOuput = new File("/temp");
		tempFolderOuput.delete();
	}

	@Test
	public void transcodeFailOnNullFFMPEGFile() {
		try {
			transcoding.transcode(null, null, null);
			fail("No should fail for null ffmpeg file");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_CONVERSION_TYPE_EMPT, e.getCodigo());
		}
	}

	@Ignore
	@Test
	public void transcodeFailOnFakeFFMPEGFile() {
		try {
			transcoding.transcode(new File("FAKE"), null, null);
			fail("No should fail for fake ffmpeg file");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FFMPEG_NOT_FOUND, e.getMessage());
		}
	}

	@Ignore
	@Test
	public void transcodeFailOnNullFolderPath() {
		try {
			transcoding.transcode(FFMPEG_PATH, null, null);
			fail("No should fail for fake input file");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FILE_INPUT_NOT_VALID, e.getMessage());
		}
	}

	@Ignore
	@Test
	public void transcodeFailOnFakeFolderOuput() {
		try {
			transcoding.transcode(FFMPEG_PATH, Paths.get("FAKE"), null);
			fail("No should fail for fake folder output");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FOLDER_OUTPUT_NOT_FOUND, e.getMessage());
		}
	}

	@Ignore
	@Test
	public void transcodeFailOnNullFolderOuput() {
		try {
			transcoding.transcode(FFMPEG_PATH, null, null);
			fail("No should fail for null folder ouput");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FOLDER_OUTPUT_NULL, e.getMessage());
		}
	}

	@Ignore
	@Test
	public void transcodeFailOnNullParams() {
		try {
			transcoding.transcode(FFMPEG_PATH, Paths.get(FOLDER_OUTPUT_REAL.toString()), null);
			fail("No should fail for null params");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_NO_CONVERSION_TYPE_FOUND, e.getMessage());
		}
	}

	@Ignore

	@Test
	public void transcodeFailOnEmptyParams() {
		try {
			transcoding.transcode(FFMPEG_PATH, Paths.get(FOLDER_OUTPUT_REAL.toString()),
					new OriginalVideo("", false, null));
			fail("No should empty params");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_CONVERSION_TYPE_EMPTY, e.getMessage());
		}
	}

	
	@Test
	public void transcodeSucess() {
		// LA CARPETA PARA LAS PRUEBAS UNITARIAS EN LA NUBE DEBE SER ESTA
		// Paths.get(FOLDER_OUTPUT_REAL.toString())

		User u1 = new User("patio@gmail.com", "admin", "pass", "", UserRoles.ADMIN, UserRoles.USER);
		OriginalVideo video = new OriginalVideo(propertiesFFmpegTest.getProperty(VIDEO_DEMO), Boolean.FALSE, u1);
		ConversionVideo newVideo = new ConversionVideo("Conversion Video 1", ConversionType.MKV_H264360_COPY, video);
		ConversionVideo newVideo2 = new ConversionVideo("Conversion Video 2", ConversionType.MKV_H2641080_COPY, video);
		List<ConversionVideo> lista = new ArrayList<>();
		lista.add(newVideo);
		lista.add(newVideo2);
		video.setAllConversions(lista);
		u1.addVideo(video);
		userService.save(u1);
		originalVideoService.save(video);
		try {
			transcoding.transcode(FFMPEG_PATH, Paths.get("/Users/luisca/Documents/VideosPrueba"), video);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// conversionVideoService.delete(x1);
			// conversionVideoService.delete(x2);
			// originalVideoService.delete(toSend);
		} catch (FFmpegException e) {
			e.printStackTrace();
			fail("No should fail");
		}
	}

}
