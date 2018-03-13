package es.urjc.videotranscoding.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component
public class FileDownloader {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private File filepath;
	private HttpServletResponse response;

	protected FileDownloader() {
	}

	private FileDownloader setFilepath(File filepath) {
		this.filepath = filepath;
		return this;
	}

	public static FileDownloader fromFile(File file) {
		return new FileDownloader().setFilepath(file);
	}

	public FileDownloader with(HttpServletResponse httpResponse) {
		response = httpResponse;
		return this;
	}

	public void serveResource() {
		try {
			String mimeType = URLConnection.guessContentTypeFromName(filepath.getName());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}
			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + filepath.getName() + "\""));
			response.setHeader("Content-Length", String.valueOf(filepath.length()));
			InputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
			FileCopyUtils.copy(inputStream, response.getOutputStream());
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}

	}
}