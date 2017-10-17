package URJC.VideoTranscoding.service.impl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import URJC.VideoTranscoding.service.ITranscodingService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FFmpegTranscodingTest {

	private final String ffmpegMac = "/usr/local/Cellar/ffmpeg/3.4/bin/ffmpeg";
	private final File uriInput = new File("/Users/luisca/Documents/VideosPrueba/StarWars.mp4");
	private final Path uriOutput = Paths.get("/Users/luisca/Documents/VideosPrueba/");
	private List<Integer> params = new ArrayList<Integer>();

	@Autowired
	private ITranscodingService transcoding;

	@Test
	public void Transcode_WithoutErrors() {
		transcoding.Transcode(ffmpegMac, uriInput, uriOutput,params);
	}
	@Test
	public void Transcode_WitoutName() {
		params.clear();
	}

}
