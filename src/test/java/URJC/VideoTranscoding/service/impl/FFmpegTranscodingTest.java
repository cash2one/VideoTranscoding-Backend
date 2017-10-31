package URJC.VideoTranscoding.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import URJC.VideoTranscoding.codecs.ConversionType;
import URJC.VideoTranscoding.exception.FFmpegException;
import URJC.VideoTranscoding.service.TranscodingService;

// TODO Cambiar mensaje de fail
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/xml/ffmpeg-config-test.xml")
public class FFmpegTranscodingTest {
	private final File FFMPEG_PATH_FAKE = new File("installation/fail");
	private final File FILE_INPUT_REAL = new File("/Users/luisca/Documents/VideosPrueba/Starwars.mp4");
	private final File FILE_INPUT_FAKE = new File("/Users/luisca/Documents/VideosPrueba/Starwa.mp4");
	private final Path FOLDER_OUTPUT_REAL = Paths.get("/Users/luisca/Documents/VideosPrueba/");
	private final Path FOLDER_OUTPUT_FAKE = Paths.get("/Users/XXXX/YYYYY");
	private final List<ConversionType> params_empty = new ArrayList<ConversionType>();
	private final List<ConversionType> params = new ArrayList<ConversionType>();
	private final String FFMPEG_INSTALLATION_MACOSX = "ffmpeg_installation_location_macosx";
	private final String FFMPEG_INSTALLATION_CENTOS7 = "ffmpeg_installation_location_centos7";
	private final String OS_MAC = "Mac OS X";
	private Map<ConversionType, Boolean> conversionFinished = new HashMap<>();
	private File FFMPEG_PATH;

	@Autowired
	private TranscodingService transcoding;
	@Resource
	Properties propertiesFFmpegTest;

	@Before
	public void setUp() {
		// TODO System.out.println("*" + System.getProperty("os.name") + "*");
		if (System.getProperty("os.name").equals(OS_MAC)) {
			FFMPEG_PATH = new File(propertiesFFmpegTest.getProperty(FFMPEG_INSTALLATION_MACOSX));
		} else {
			FFMPEG_PATH = new File(propertiesFFmpegTest.getProperty(FFMPEG_INSTALLATION_CENTOS7));
		}
	}

	@BeforeClass
	public static void beforeClass() {
	}

	@After
	public void setDown() {
	}

	@AfterClass
	public static void afterClass() {
	}

	@Test
	public void transcodeFailOnFakeFFMPEGFile() {
		try {
			transcoding.transcode(FFMPEG_PATH_FAKE, null, null, null);
			fail("No should fail");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FFMPEG_NOT_FOUND, e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnNullFFMPEGFile() {
		try {
			transcoding.transcode(null, null, null, null);
			fail("No should fail");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FFMPEG_NOT_FOUND, e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnFakeInputFile() {
		try {
			transcoding.transcode(FFMPEG_PATH, FILE_INPUT_FAKE, null, null);
			fail("No should fail");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FILE_INPUT_NOT_VALID, e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnNullInputFile() {
		try {
			transcoding.transcode(FFMPEG_PATH, null, null, null);
			fail("No deberia fallar");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FILE_INPUT_NOT_VALID, e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnFakeFolderOuput() {
		try {
			transcoding.transcode(FFMPEG_PATH, FILE_INPUT_REAL, FOLDER_OUTPUT_FAKE, null);
			fail("No deberia fallar");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FOLDER_OUTPUT_NOT_FOUND, e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnNullFolderOuput() {
		try {
			transcoding.transcode(FFMPEG_PATH, FILE_INPUT_REAL, null, null);
			fail("No deberia fallar");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FOLDER_OUTPUT_NULL, e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnNullParams() {
		try {
			transcoding.transcode(FFMPEG_PATH, FILE_INPUT_REAL, FOLDER_OUTPUT_REAL, null);
			fail("No deberia fallar");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_NO_CONVERSION_TYPE_FOUND, e.getMessage());
		}
	}

	@Test
	public void transcodeFailOnEmptyParams() {
		try {
			transcoding.transcode(FFMPEG_PATH, FILE_INPUT_REAL, FOLDER_OUTPUT_REAL, params_empty);
			fail("No deberia fallar");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_CONVERSION_TYPE_EMPTY, e.getMessage());
		}
	}

	@Ignore
	@Test
	public void transcodeSucess() throws FFmpegException {
		// params.add(ConversionType.MKV_HEVC360_COPY);
		params.add(ConversionType.WEBM_VP91080_VORBIS);
		params.add(ConversionType.MKV_H2641080_COPY);
		conversionFinished = transcoding.transcode(FFMPEG_PATH, FILE_INPUT_REAL, FOLDER_OUTPUT_REAL, params);
		if (conversionFinished.containsValue(false)) {
			fail("Algo ha ido mal");
		}
	}
}
