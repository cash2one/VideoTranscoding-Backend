package URJC.VideoTranscoding.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.springframework.stereotype.Service;

import URJC.VideoTranscoding.service.Transcoding;

@Service
class FfmpegTranscodingImpl implements Transcoding {

	public void Transcode(String pathFFMPEG, String fileInput, String folderOutput) {
		try {
			String sort = String.valueOf(System.currentTimeMillis());
			
			String[] commmand = new String[] { pathFFMPEG, "-i", fileInput, folderOutput + sort.substring(1,5) + ".mkv" };
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
				Thread.sleep(1000);
			}
			System.out.println(process.exitValue());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
