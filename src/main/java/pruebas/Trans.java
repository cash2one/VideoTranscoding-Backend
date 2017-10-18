package pruebas;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

public class Trans {
	public void transProcessBuilder(String command) throws IOException, InterruptedException {
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		ProcessBuilder builder = new ProcessBuilder();
		if (isWindows) {
			builder.command("cmd.exe", "/c", "dir");
		} else {
			builder.command("sh", "-c", command);
		}
		builder.directory(new File(System.getProperty("user.home")));
		Process process = builder.start();
		StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
		Executors.newSingleThreadExecutor().submit(streamGobbler);
		int exitCode = process.waitFor();
		assert exitCode == 0;
	}

	public void transRuntime(String command) throws IOException, InterruptedException {
		// A Runtime object has methods for dealing with the OS
		Runtime r = Runtime.getRuntime();
		Process p; // Process tracks one external native process
		BufferedReader is; // reader for output of process
		String line;

		// Our argv[0] contains the program to run; remaining elements
		// of argv contain args for the target program. This is just
		// what is needed for the String[] form of exec.
		p = r.exec(command);

		System.out.println("In Main after exec");

		// getInputStream gives an Input stream connected to
		// the process p's standard output. Just use it to make
		// a BufferedReader to readLine() what the program writes out.
		is = new BufferedReader(new InputStreamReader(p.getInputStream()));

		while ((line = is.readLine()) != null)
			System.out.println(line);

		System.out.println("In Main after EOF");
		System.out.flush();
		try {
			p.waitFor(); // wait for process to complete
		} catch (InterruptedException e) {
			System.err.println(e); // "Can'tHappen"
			return;
		}
		System.err.println("Process done, exit status was " + p.exitValue());
		return;
	}
}
