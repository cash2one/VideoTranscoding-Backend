package URJC.VideoTranscoding.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import URJC.VideoTranscoding.service.Transcoding;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FfmpegTranscodingImplTest {

	private final String ffmpegMac = "/usr/local/Cellar/ffmpeg/3.4/bin/ffmpeg";
	private final String uriInput = "/Users/luisca/Documents/TFG17-18/VideosPrueba/Star.mp4";
	private final String uriOutput = "/Users/luisca/Documents/TFG17-18/VideosPrueba/";
	private List<Integer> params = new ArrayList<Integer>();

	@Autowired
	private Transcoding transcoding;

	@Test
	public void test() {
		transcoding.Transcode(ffmpegMac, uriInput, uriOutput);

		// fail("Not yet implemented");
	}

}
