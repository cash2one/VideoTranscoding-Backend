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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import urjc.videotranscoding.app.test.AppTest;
import urjc.videotranscoding.codecs.ConversionType;
import urjc.videotranscoding.entities.ConversionVideo;
import urjc.videotranscoding.entities.OriginalVideo;
import urjc.videotranscoding.exception.FFmpegException;
import urjc.videotranscoding.persistentffmpeg.TranscodingServicePersistent;
import urjc.videotranscoding.service.ConversionVideoService;
import urjc.videotranscoding.service.OriginalVideoService;


@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest(classes = AppTest.class)
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
	public void transcodeFailOnNullFFMPEGFile() {
		try {
			transcoding.transcode(null, null, null);
			fail("No should fail for null ffmpeg file");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FFMPEG_NOT_FOUND, e.getMessage());
		}
	}
	@Test
	public void transcodeFailOnFakeFFMPEGFile() {
		try {
			transcoding.transcode(new File("FAKE"), null, null);
			fail("No should fail for fake ffmpeg file");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FFMPEG_NOT_FOUND, e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnNullFolderPath() {
		try {
			transcoding.transcode(FFMPEG_PATH, null, null);
			fail("No should fail for fake input file");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FILE_INPUT_NOT_VALID,
					e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnFakeFolderOuput() {
		try {
			transcoding.transcode(FFMPEG_PATH, Paths.get("FAKE"), null);
			fail("No should fail for fake folder output");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FOLDER_OUTPUT_NOT_FOUND,
					e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnNullFolderOuput() {
		try {
			transcoding.transcode(FFMPEG_PATH, null, null);
			fail("No should fail for null folder ouput");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FOLDER_OUTPUT_NULL, e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnNullParams() {
		try {
			transcoding.transcode(FFMPEG_PATH,
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
					Paths.get(FOLDER_OUTPUT_REAL.toString()),
					new OriginalVideo("", false, null));
			fail("No should empty params");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_CONVERSION_TYPE_EMPTY,
					e.getMessage());
		}
	}
	@Ignore
	@Test
	public void transcodeSucess() {
		// LA CARPETA PARA LAS PRUEBAS UNITARIAS EN LA NUBE DEBE SER ESTA
		// Paths.get(FOLDER_OUTPUT_REAL.toString())
		OriginalVideo toSend = new OriginalVideo(
				propertiesFFmpegTest.getProperty(VIDEO_DEMO), false, null);
		ConversionVideo x1 = new ConversionVideo("x1",
				ConversionType.MKV_H264360_COPY, toSend);
		ConversionVideo x2 = new ConversionVideo("x2",
				ConversionType.MKV_H264360_COPY, toSend);
		List<ConversionVideo> setConversion = new ArrayList<>();
		setConversion.add(x2);
		setConversion.add(x1);

		toSend.setAllConversions(setConversion);
		conversionVideoService.save(x1);
		conversionVideoService.save(x2);
		originalVideoService.save(toSend);
		try {
			transcoding.transcode(FFMPEG_PATH,
					Paths.get("/Users/luisca/Documents/VideosPrueba"), toSend);
			conversionVideoService.delete(x1);
			conversionVideoService.delete(x2);
			originalVideoService.delete(toSend);
		} catch (FFmpegException e) {
			e.printStackTrace();
			fail("No should fail");
		}
	}

}
