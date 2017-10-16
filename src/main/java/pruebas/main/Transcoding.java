package pruebas.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Transcoding {
	private final String uri = "/Users/luisca/Documents/TFG17-18/VideosPrueba/Star.mp4";
	private final String searchFfmpegMac = "find / -type f -name ffmpeg";
	private final String ffmpegMac = "/usr/local/Cellar/ffmpeg/3.3.4/bin/ffmpeg";
	private final String code = "Process process = Runtime.getRuntime().exec(command);\n" + "process.waitFor();";

	public Transcoding() {
	}

	public void sendTranscoding(File file) {
		Process pr;
		try {
			pr = Runtime.getRuntime().exec("java -version");
			BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			pr.waitFor();

			System.out.println("ok!");

			in.close();
			System.exit(0);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void sendTranscoding4(File file) {
		try {
			Process processDuration = new ProcessBuilder(ffmpegMac, "-version").redirectErrorStream(true).start();
			StringBuilder strBuild = new StringBuilder();
			try (BufferedReader processOutputReader = new BufferedReader(
					new InputStreamReader(processDuration.getInputStream(), Charset.defaultCharset()));) {
				String line;
				while ((line = processOutputReader.readLine()) != null) {
					strBuild.append(line + System.lineSeparator());
				}
				processDuration.waitFor();
			}
			String outputJson = strBuild.toString().trim();
			System.out.println(outputJson);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void sendTranscoding5(File file) {
		String command = "ffmpeg -y -i video.mp4 -c:v mpeg4 -b:v 600k -c:a libmp3lame output.avi";
	}

}
