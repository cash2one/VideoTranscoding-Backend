package URJC.VideoTranscoding.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import URJC.VideoTranscoding.codecs.AudioCodec;
import URJC.VideoTranscoding.codecs.VideoCodec;
import URJC.VideoTranscoding.exception.FFmpegException;
import URJC.VideoTranscoding.service.ITranscodingService;
import pruebas.StreamGobbler;

@Service
class FFmpegTranscoding implements ITranscodingService {

	public void Transcode(String pathFFMPEG, File fileInput, Path folderOutput, List<Integer> conversionType)
			throws FFmpegException {
		// TODO throw PATHFFMPEG
		if (!fileInput.exists()) {
			FFmpegException ex = new FFmpegException(FFmpegException.EX_FILE_INPUT_NOT_VALID);
			throw ex;
		}
		Trans2(pathFFMPEG, fileInput, folderOutput, conversionType);
	}

	private void Trans2(String pathFFMPEG, File fileInput, Path folderOutput, List<Integer> conversionType) {
		try {
			String sort = String.valueOf(System.currentTimeMillis());
			// String[] command = new String[] { " -c ", pathFFMPEG, " -i ",
			// fileInput.toString(),
			// " -c:a " + AudioCodec.LIBVORBIS, " -c:v " + VideoCodec.VP9, " " +
			// folderOutput + "/"
			// + FilenameUtils.getBaseName(fileInput.getName()) + sort.substring(4, 8) +
			// ".webm" };
			// String[] command2 = new String[] { pathFFMPEG +" -i "+ fileInput.toString()+
			// AudioCodec.LIBVORBIS+
			// VideoCodec.VP9+ folderOutput + "/" +
			// FilenameUtils.getBaseName(fileInput.getName())
			// + sort.substring(4, 8) + ".webm" };

			String commandF = pathFFMPEG + fileInput.toString() + AudioCodec.LIBVORBIS + VideoCodec.VP9 + folderOutput
					+ "/" + FilenameUtils.getBaseName(fileInput.getName()) + sort.substring(4, 8) + ".webm";

			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(commandF);

			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
			StreamGobbler inputGobbler = new StreamGobbler(proc.getInputStream(), "INPUT");
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
			inputGobbler.start();
			errorGobbler.start();
			outputGobbler.start();

			int exitVal;

			exitVal = proc.waitFor();

			System.out.println("ExitValue: " + exitVal);

		} catch (ExecuteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void Trans(String pathFFMPEG, File fileInput, Path folderOutput, List<Integer> conversionType) {
		try {
			String sort = String.valueOf(System.currentTimeMillis());
			String[] commmand = new String[] { pathFFMPEG, " -i ", fileInput.toString(),
					" -c:a " + AudioCodec.LIBVORBIS, " -c:v " + VideoCodec.VP9, " " + folderOutput + "/"
							+ FilenameUtils.getBaseName(fileInput.getName()) + sort.substring(4, 8) + ".webm" };
			for (String string : commmand) {
				System.out.print(string);
			}
			ProcessBuilder p = new ProcessBuilder(commmand);

			p.redirectErrorStream(true);
			Process process = p.start();
			InputStream out = process.getInputStream();
			InputStream error = process.getErrorStream();
			OutputStream in = process.getOutputStream();

			byte[] buffer = new byte[4000];
			while (isAlive(process)) {
				int no = out.available();
				if (no > 0) {
					int n = out.read(buffer, 0, Math.min(no, buffer.length));
					System.out.println(new String(buffer, 0, n));
				}
				int noe = error.available();
				if (noe > 0) {
					int n = error.read(buffer, 0, Math.min(noe, buffer.length));
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
