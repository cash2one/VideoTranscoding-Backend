package urjc.videotranscoding.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import urjc.videotranscoding.App;
import urjc.videotranscoding.codecs.ConversionType;
import urjc.videotranscoding.entities.ConversionVideo;
import urjc.videotranscoding.entities.OriginalVideo;
import urjc.videotranscoding.exception.FFmpegException;
import urjc.videotranscoding.persistentffmpeg.TranscodingServicePersistent;
import urjc.videotranscoding.service.ConversionVideoService;
import urjc.videotranscoding.service.OriginalVideoService;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = { JpaConfig.class }, loader = AnnotationConfigContextLoader.class)
//@Transactional
@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = App.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class FFmpegTranscodingPersistentTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	private final String FFMPEG_INSTALLATION_CENTOS7 = "path.ffmpeg.centos";
	private final String FFMPEG_INSTALLATION_MACOSX = "path.ffmpeg.macosx";
	private final String VIDEO_DEMO = "path.video.demo";
	private final String OS_MAC = "Mac OS X";
	private static File FFMPEG_PATH;
	private File FOLDER_OUTPUT_REAL;
	@Autowired
	private TranscodingServicePersistent transcoding;
	@Autowired
	private ConversionVideoService conversionVideoService;
	@Autowired
	private OriginalVideoService originalVideoService;
	@Resource
	private Properties propertiesFFmpegTest;

	@BeforeClass
	public static void beforeClass() {
		System.setProperty("log4j.configurationFile", "log4j2-test.xml");
	}

	@Before
	public void setUp() throws IOException {
		if (System.getProperty("os.name").equals(OS_MAC)) {
			FFMPEG_PATH = new File(propertiesFFmpegTest
					.getProperty(FFMPEG_INSTALLATION_MACOSX));
		} else {
			FFMPEG_PATH = new File(propertiesFFmpegTest
					.getProperty(FFMPEG_INSTALLATION_CENTOS7));
		}
		FOLDER_OUTPUT_REAL = folder.newFolder("temp");
	}

	@AfterClass
	public static void afterClass() {
		File tempFolderOuput = new File("/temp");
		tempFolderOuput.delete();
	}

	@Test
	public void transcodeFailOnFakeFFMPEGFile() {
		try {
			transcoding.transcode(new File("FAKE"), null, null, null);
			fail("No should fail for fake ffmpeg file");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FFMPEG_NOT_FOUND, e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnNullFFMPEGFile() {
		try {
			transcoding.transcode(null, null, null, null);
			fail("No should fail for null ffmpeg file");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FFMPEG_NOT_FOUND, e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnFakeInputFile() {
		try {
			transcoding.transcode(FFMPEG_PATH, new File("FAKE"), null, null);
			fail("No should fail for fake input file");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FILE_INPUT_NOT_VALID,
					e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnNullInputFile() {
		try {
			transcoding.transcode(FFMPEG_PATH, null, null, null);
			fail("No should fail for null input file");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FILE_INPUT_NOT_VALID,
					e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnFakeFolderOuput() {
		try {
			transcoding.transcode(FFMPEG_PATH,
					new File(propertiesFFmpegTest.getProperty(VIDEO_DEMO)),
					Paths.get("FAKE"), null);
			fail("No should fail for fake folder output");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FOLDER_OUTPUT_NOT_FOUND,
					e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnNullFolderOuput() {
		try {
			transcoding.transcode(FFMPEG_PATH,
					new File(propertiesFFmpegTest.getProperty(VIDEO_DEMO)),
					null, null);
			fail("No should fail for null folder ouput");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FOLDER_OUTPUT_NULL, e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnNullParams() {
		try {
			transcoding.transcode(FFMPEG_PATH,
					new File(propertiesFFmpegTest.getProperty(VIDEO_DEMO)),
					Paths.get(FOLDER_OUTPUT_REAL.toString()), null);
			fail("No should fail for null params");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_NO_CONVERSION_TYPE_FOUND,
					e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnEmptyParams() {
		try {
			transcoding.transcode(FFMPEG_PATH,
					new File(propertiesFFmpegTest.getProperty(VIDEO_DEMO)),
					Paths.get(FOLDER_OUTPUT_REAL.toString()),
					new OriginalVideo("", false));
			fail("No should empty params");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_CONVERSION_TYPE_EMPTY,
					e.getMessage());
		}
	}

	@Ignore
	@Test
	public void transcodeSucess() {
		OriginalVideo toSend = new OriginalVideo("VIDEO", false);
		ConversionVideo x1 = new ConversionVideo("x1",
				ConversionType.MKV_H264360_COPY, toSend);
		ConversionVideo x2 = new ConversionVideo("x2",
				ConversionType.MKV_H264360_COPY, toSend);
		toSend.setAllConversions(Arrays.asList(x1, x2));
		conversionVideoService.save(x1);
		conversionVideoService.save(x2);
		originalVideoService.save(toSend);
		try {

			transcoding.transcode(FFMPEG_PATH,
					new File(propertiesFFmpegTest.getProperty(VIDEO_DEMO)),
					Paths.get(FOLDER_OUTPUT_REAL.toString()), toSend);
		} catch (FFmpegException e) {
			e.printStackTrace();
			fail("No should fail");
		} finally {
			conversionVideoService.delete(x1);
			conversionVideoService.delete(x2);
			originalVideoService.delete(toSend);
		}
	}

}
