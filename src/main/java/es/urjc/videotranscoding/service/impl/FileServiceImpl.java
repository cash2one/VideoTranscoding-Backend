package es.urjc.videotranscoding.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.core.impl.VideoTranscodingFFmpegImpl;
import es.urjc.videotranscoding.exception.FFmpegException;
import es.urjc.videotranscoding.service.FileService;
import es.urjc.videotranscoding.service.FileUtils;
import es.urjc.videotranscoding.wrapper.FfmpegResourceBundle;

@Service
public class FileServiceImpl implements FileService{
	private static final Logger logger = Logger.getLogger(VideoTranscodingFFmpegImpl.class);

	private static final String FICH_TRAZAS = "fichero.mensajes.trazas";

	private static final String TRACE_FOLDER_OUTPUT_NULL_OR_EMPTY = "ffmpeg.folderOuput.nullOrEmpty";
	private static final String TRACE_FOLDER_OUPUT_NOT_EXISTS = "ffmpeg.folderOutput.notExits";
	@Resource
	private Properties propertiesFicheroCore;

	@Resource
	private Properties propertiesFFmpeg;
	private final static String FOLDER_OUPUT_ORIGINAL="path.folder.original";
	@Autowired
	private FileUtils fileUtils;
	@Resource 
	private FfmpegResourceBundle ffmpegResourceBundle;
	/**
	 * @param file
	 * @param folderOutput
	 * @return
	 */
	@PostConstruct
	public void init() {
		// propertiesFicheroCore = (Properties)
		// ApplicationContextProvider.getApplicationContext()
		// .getBean("propertiesFicheroCore");
		logger.setResourceBundle(ffmpegResourceBundle
				.getFjResourceBundle(propertiesFicheroCore.getProperty(FICH_TRAZAS), Locale.getDefault()));
	}
	public File saveFile(MultipartFile file) throws FFmpegException{
		try{
			String folderOutputOriginalVideo=propertiesFFmpeg.getProperty(FOLDER_OUPUT_ORIGINAL);
			if (StringUtils.isBlank(folderOutputOriginalVideo)) {
				logger.l7dlog(Level.ERROR, TRACE_FOLDER_OUTPUT_NULL_OR_EMPTY, null);
				throw new FFmpegException(FFmpegException.EX_FOLDER_OUTPUT_EMPTY_OR_NULL);
			}
			if (!fileUtils.exitsPath(folderOutputOriginalVideo)) {
				logger.l7dlog(Level.ERROR, TRACE_FOLDER_OUPUT_NOT_EXISTS, new String[] { folderOutputOriginalVideo }, null);
				throw new FFmpegException(FFmpegException.EX_FOLDER_OUTPUT_NOT_EXITS, new String[] { folderOutputOriginalVideo });
			}
			byte[] bytes = file.getBytes();
			Path path = Paths.get( folderOutputOriginalVideo+ file.getOriginalFilename().replace(" ","_"));
			
			Files.write(path,bytes);
			return path.toFile();
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
}
