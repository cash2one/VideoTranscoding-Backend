package URJC.VideoTranscoding.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import URJC.VideoTranscoding.codecs.ConversionType;
import URJC.VideoTranscoding.exception.FFmpegException;
import URJC.VideoTranscoding.service.ITranscodingService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/xml/ffmpeg-config-test.xml")
public class FFmpegTranscodingTest {

	private final String ffmpegMac = "/usr/local/Cellar/ffmpeg/3.4/bin/ffmpeg -i ";
	private final File fileInput = new File("/Users/luisca/Documents/VideosPrueba/Starwars.mp4");
	private final Path folderOutput = Paths.get("/Users/luisca/Documents/VideosPrueba/");
	private List<ConversionType> params = new ArrayList<ConversionType>();

	@Autowired
	private ITranscodingService transcoding;

	@Test
	public void Transcode_WithFailOnInputFile() {
		try {
			transcoding.Transcode(ffmpegMac, new File("/Users/luisca/Documents/VideosPrueba/Sta.mp4"), folderOutput,
					params);
			fail("No deberia fallar");
		} catch (FFmpegException e) {
			assertEquals(FFmpegException.EX_FILE_INPUT_NOT_VALID, e.getMessage());
		}
	}

	@Test
	public void Transcode_WithoutErrors() throws FFmpegException {
		params.add(ConversionType.MKV_HEVC1080_COPY);
		transcoding.Transcode(ffmpegMac, fileInput, folderOutput, params);
	}

}
