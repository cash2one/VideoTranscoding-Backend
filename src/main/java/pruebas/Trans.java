package pruebas;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;

public class Trans {

	public void Transcode(String pathFFMPEG, String fileInput, String fileOutput) {
		try {
			String[] commmand = new String[] { pathFFMPEG, "-i", fileInput, fileOutput };

			Process processDuration = new ProcessBuilder(commmand).redirectErrorStream(true).start();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(processDuration.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
			}
			String result = builder.toString();

//			StringBuilder strBuild = new StringBuilder();
//			BufferedReader processOutputReader = new BufferedReader(
//					new InputStreamReader(processDuration.getErrorStream(), Charset.defaultCharset()));
//
//			String line;
//			while ((line = processOutputReader.readLine()) != null) {
//				strBuild.append(line + System.lineSeparator());
//			}
//			processDuration.waitFor();
//
//			String outputJson = strBuild.toString().trim();
//			System.out.println(outputJson);

		} catch (IOException e) {
			e.printStackTrace();
		} 

	}

	public void tran(String pathFFMPEG, String fileInput, String fileOutput) {
		try {
			String[] commmand = new String[] { pathFFMPEG, "-i", fileInput, fileOutput };

			Process p = Runtime.getRuntime().exec(commmand);
			Thread outHandler = new OutputHandler(p.getInputStream(), "UTF-8");
			outHandler.setDaemon(true);
			outHandler.start();
			Thread errHandler = new OutputHandler(p.getErrorStream(), "UTF-8");
			errHandler.setDaemon(true);
			errHandler.start();
			sendInput(p, "the input data", "UTF-8");
			int result = p.waitFor();
			outHandler.join();
			errHandler.join();
			System.out.println("exit code: " + result);
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	private void sendInput(Process process, String input, String encoding) throws IOException {

		try (OutputStream stream = process.getOutputStream();
				Writer writer = new OutputStreamWriter(stream, encoding == null ? "UTF-8" : encoding);
				PrintWriter out = new PrintWriter(writer)) {
			if (input != null) {
				Reader reader = new StringReader(input);
				BufferedReader in = new BufferedReader(reader);
				String line = in.readLine();
				while (line != null) {
					out.println(line);
					line = in.readLine();
				}
			}
		}
	}

	private static class OutputHandler extends Thread {
		private BufferedReader in;

		private OutputHandler(InputStream in, String encoding) throws UnsupportedEncodingException {
			this.in = new BufferedReader(new InputStreamReader(in, encoding == null ? "UTF-8" : encoding));
		}

		@Override
		public void run() {
			try {
				String s = in.readLine();
				while (s != null) {
					System.out.println(s);
					s = in.readLine();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
