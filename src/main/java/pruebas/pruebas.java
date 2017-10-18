package pruebas;

import java.io.IOException;

@SuppressWarnings("unused")
public class pruebas {

	public static void main(String[] args) throws InterruptedException, IOException {
		String command2 = "/usr/local/Cellar/ffmpeg/3.4/bin/ffmpeg -i /Users/luisca/Documents/VideosPrueba/StarWars.mp4 -c:a copy -c:v libx265 -crf 15 -preset slow /Users/luisca/Documents/VideosPrueba/StarWars2.mkv";

		String hevc2160 = "/usr/local/Cellar/ffmpeg/3.4/bin/ffmpeg -i /Users/luisca/Documents/VideosPrueba/StarWars.mp4 -c:a copy -c:v libx265 -s 3840x2160 -crf 15 -preset slow /Users/luisca/Documents/VideosPrueba/StarWars2.mkv";
		String hevc1440 = "/usr/local/Cellar/ffmpeg/3.4/bin/ffmpeg -i /Users/luisca/Documents/VideosPrueba/StarWars.mp4 -c:a copy -c:v libx265 -s 2560×1440 -crf 15 -preset slow /Users/luisca/Documents/VideosPrueba/StarWars22.mkv";
		String hevc1080 = "/usr/local/Cellar/ffmpeg/3.4/bin/ffmpeg -i /Users/luisca/Documents/VideosPrueba/StarWars.mp4 -c:a copy -c:v libx265 -s 1920x1080 -crf 15 -preset slow /Users/luisca/Documents/VideosPrueba/StarWars222.mkv";
		String hevc720 = "/usr/local/Cellar/ffmpeg/3.4/bin/ffmpeg -i /Users/luisca/Documents/VideosPrueba/StarWars.mp4 -c:a copy -c:v libx265 -s 1280x720 -crf 15 -preset slow /Users/luisca/Documents/VideosPrueba/StarWars2222.mkv";
		String hevc480 = "/usr/local/Cellar/ffmpeg/3.4/bin/ffmpeg -i /Users/luisca/Documents/VideosPrueba/StarWars.mp4 -c:a copy -c:v libx265 -b:v 2600k -s 640x480 -crf 15 -preset slow /Users/luisca/Documents/VideosPrueba/StarWars222wd22.mkv";
		String hevc360 = "/usr/local/Cellar/ffmpeg/3.4/bin/ffmpeg -i /Users/luisca/Documents/VideosPrueba/StarWars.mp4 -c:a copy -c:v libx265 -s 480x360 -crf 15 -preset slow /Users/luisca/Documents/VideosPrueba/StarWars222222.mkv";

		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(hevc480);

			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
			StreamGobbler inputGobbler = new StreamGobbler(proc.getInputStream(), "INPUT");
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
			inputGobbler.start();
			errorGobbler.start();
			outputGobbler.start();

			int exitVal = proc.waitFor();
			System.out.println("ExitValue: " + exitVal);

		} catch (Throwable t) {
			t.printStackTrace();
		}

		// CommandLine command = new CommandLine("/bin/sh");
		// command.addArguments(new String[] { "-c",command2 }, false);
		// new DefaultExecutor().execute(command);

		// Trans x= new Trans();
		// x.transRuntime(command);
	}

}
