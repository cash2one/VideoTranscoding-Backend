package URJC.VideoTranscoding.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import URJC.VideoTranscoding.service.ITranscodingService;

@Service
class FFmpegTranscoding implements ITranscodingService {

	public void Transcode(String pathFFMPEG, File fileInput, Path folderOutput, List<Integer> conversionType) {
		if (StringUtils.isEmpty(pathFFMPEG)) {

		}
		Trans(pathFFMPEG, fileInput, folderOutput, conversionType);
	}

	private void Trans(String pathFFMPEG, File fileInput, Path folderOutput, List<Integer> conversionType) {
		try {
			String sort = String.valueOf(System.currentTimeMillis());
			String[] commmand = new String[] { pathFFMPEG, "-i", fileInput.toString(), folderOutput + "/"
					+ FilenameUtils.getBaseName(fileInput.getName()) + sort.substring(4, 8) + ".mkv" };
			ProcessBuilder p = new ProcessBuilder(commmand);
			p.redirectErrorStream(true);
			Process process = p.start();
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
			// TODO
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO
			e.printStackTrace();
		}
	}

	private boolean isAlive(Process p) {
		try {
			p.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}

}
