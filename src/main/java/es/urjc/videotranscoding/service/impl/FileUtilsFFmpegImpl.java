package es.urjc.videotranscoding.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.codecs.Container;
import es.urjc.videotranscoding.exception.FFmpegException;
import es.urjc.videotranscoding.service.FileUtilsFFmpeg;
import es.urjc.videotranscoding.wrapper.FfmpegResourceBundle;

@Service
public class FileUtilsFFmpegImpl implements FileUtilsFFmpeg {
	private static final Logger logger = Logger.getLogger(FileUtilsFFmpegImpl.class);
	private static final String FICH_TRAZAS = "fichero.mensajes.trazas";
	private static final String TRACE_FOLDER_OUTPUT_NULL_OR_EMPTY = "ffmpeg.folderOuput.nullOrEmpty";
	private static final String TRACE_FOLDER_OUPUT_NOT_EXISTS = "ffmpeg.folderOutput.notExits";
	private static final String TRACE_NOT_VIDEO_FILE = "ffmpeg.noVideoFile";
	private static final String TRACE_VIDEO_EXISTS = "ffmpeg.fileOriginal.exists";
	private static final String TRACE_IO_EXCEPTION_GENERAL = "ffmpeg.ioException.general";
	private static final String FOLDER_OUPUT_ORIGINAL = "path.folder.original";

	@Resource
	private Properties propertiesFicheroCore;
	@Resource
	private Properties propertiesFFmpeg;
	@Resource
	private FfmpegResourceBundle ffmpegResourceBundle;

	@PostConstruct
	public void init() {
		logger.setResourceBundle(ffmpegResourceBundle
				.getFjResourceBundle(propertiesFicheroCore.getProperty(FICH_TRAZAS), Locale.getDefault()));
	}

	public File saveFile(MultipartFile file) throws FFmpegException {
		try {
			String folderOutputOriginalVideo = propertiesFFmpeg.getProperty(FOLDER_OUPUT_ORIGINAL);
			if (StringUtils.isBlank(folderOutputOriginalVideo)) {
				logger.l7dlog(Level.ERROR, TRACE_FOLDER_OUTPUT_NULL_OR_EMPTY, null);
				throw new FFmpegException(FFmpegException.EX_FOLDER_OUTPUT_EMPTY_OR_NULL);
			}
			if (!exitsDirectory(folderOutputOriginalVideo)) {
				logger.l7dlog(Level.ERROR, TRACE_FOLDER_OUPUT_NOT_EXISTS, new String[] { folderOutputOriginalVideo },
						null);
				throw new FFmpegException(FFmpegException.EX_FOLDER_OUTPUT_NOT_EXITS,
						new String[] { folderOutputOriginalVideo });
			}
			if (!isVideoFile(file)) {
				logger.l7dlog(Level.ERROR, TRACE_NOT_VIDEO_FILE, new String[] { file.getOriginalFilename() }, null);
				throw new FFmpegException(FFmpegException.EX_NOT_VIDEO_FILE,
						new String[] { file.getOriginalFilename() });
			}
			Path path = Paths.get(folderOutputOriginalVideo + file.getOriginalFilename().replace(" ", "_"));
			File f = path.toFile().getAbsoluteFile();
			if (f.exists()) {
				logger.l7dlog(Level.ERROR, TRACE_VIDEO_EXISTS, new String[] { file.getName() }, null);
				throw new FFmpegException(FFmpegException.EX_VIDEO_EXITS, new String[] { file.getOriginalFilename() });
			}
			InputStream fileStream = file.getInputStream();
			FileUtils.copyInputStreamToFile(fileStream, f);
			return path.toFile();
		} catch (IOException e) {
			logger.l7dlog(Level.ERROR, TRACE_IO_EXCEPTION_GENERAL, e);
			throw new FFmpegException(FFmpegException.EX_IO_EXCEPTION_GENERAL, e);
		}
	}

	private boolean isVideoFile(MultipartFile file) {
		String nameExtension = FilenameUtils.getExtension(file.getOriginalFilename());
		if (nameExtension == "")
			return false;
		boolean exists = Arrays.asList(Container.values()).stream()
				.anyMatch(container -> container.toString().contains(nameExtension));
		if (exists) {
			return true;
		}
		return false;
	}

	public boolean exitsFile(String file) {
		File f = new File(file);
		return f.exists();
	}

	public boolean exitsDirectory(String path) {
		File f = new File(path);
		return f.isDirectory();
	}

	public boolean deleteFile(String file) {
		if (file == null) {
			return true;
		}
		File f = new File(file);
		if (f.exists()) {
			f.delete();
			return true;
		}
		return false;
	}

}
