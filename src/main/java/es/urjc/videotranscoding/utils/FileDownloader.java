package es.urjc.videotranscoding.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import es.urjc.videotranscoding.core.impl.VideoTranscodingFFmpegImpl;
import es.urjc.videotranscoding.exception.FFmpegException;

/**
 * This Class is used for downloading the file
 * 
 * @author luisca
 * @since 0.5
 */
@Component
public class FileDownloader {
	private static final String TRACE_IO_EXCEPTION_GENERAL = "ffmpeg.ioException.general";
	protected final Logger logger = Logger.getLogger(VideoTranscodingFFmpegImpl.class);
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

	public void serveResource() throws FFmpegException {
		try {
			String mimeType = "notDefined/octet-stream";
			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + filepath.getName() + "\""));
			response.setHeader("Content-Length", String.valueOf(filepath.length()));
			InputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
			FileCopyUtils.copy(inputStream, response.getOutputStream());
		} catch (IOException e) {
			logger.l7dlog(Level.ERROR, TRACE_IO_EXCEPTION_GENERAL, e);
			throw new FFmpegException(FFmpegException.EX_IO_EXCEPTION_GENERAL);
		}

	}
}