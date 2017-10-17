package pruebas;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Trans2 {

	public Trans2() {
	}

	public void transcode(String pathFFMPEG, String fileInput, String folderOutput)
			throws InterruptedException, IOException {
		String[] commmand = new String[] { pathFFMPEG, "-i", fileInput, folderOutput+System.currentTimeMillis()+".mkv" };
		ProcessBuilder builder = new ProcessBuilder(commmand);
		builder.redirectErrorStream(true); // so we can ignore the error stream
		Process process = builder.start();
		InputStream out = process.getInputStream();
		OutputStream in = process.getOutputStream();

		byte[] buffer = new byte[4000];
		while (isAlive(process)) {
			int no = out.available();
			if (no > 0) {
				int n = out.read(buffer, 0, Math.min(no, buffer.length));
				System.out.println(new String(buffer, 0, n));
			}
			int ni = System.in.available();
			if (ni > 0) {
				int n = System.in.read(buffer, 0, Math.min(ni, buffer.length));
				in.write(buffer, 0, n);
				in.flush();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(process.exitValue());
	}

	public boolean isAlive(Process p) {
		try {
			p.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}

}
