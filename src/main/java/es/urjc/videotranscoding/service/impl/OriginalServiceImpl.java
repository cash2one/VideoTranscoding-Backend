package es.urjc.videotranscoding.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.codecs.ConversionType;
import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.exception.FFmpegException;
import es.urjc.videotranscoding.repository.OriginalRepository;
import es.urjc.videotranscoding.service.OriginalService;
import es.urjc.videotranscoding.wrapper.FfmpegResourceBundle;

@Service
public class OriginalServiceImpl implements OriginalService {
	private static final Logger logger = Logger.getLogger(OriginalServiceImpl.class);
	private static final String FICH_TRAZAS = "fichero.mensajes.trazas";
	private static final String TRACE_NO_CONVERSION_TYPE_FOUND = "ffmpeg.conversionType.notFound";
	@Autowired
	private OriginalRepository originalVideoRepository;
	@Autowired
	private FileUtilsFFmpegImpl fileUtilsService;

	@Resource
	private FfmpegResourceBundle ffmpegResourceBundle;
	@Resource
	private Properties propertiesFicheroCore;
	// TODO JAVADOC, LOGGER, EXCEPTS

	@PostConstruct
	public void init() {
		logger.setResourceBundle(ffmpegResourceBundle
				.getFjResourceBundle(propertiesFicheroCore.getProperty(FICH_TRAZAS), Locale.getDefault()));
	}

	public void save(Original video) {
		originalVideoRepository.save(video);
	}

	public void delete(Original video) {
		originalVideoRepository.delete(video);
	}

	public void delete(long id) {
		originalVideoRepository.deleteById(id);
	}

	public List<Original> findAllVideos() {
		return originalVideoRepository.findAll();
	}

	public Optional<Original> findOneVideo(long id) {
		return originalVideoRepository.findById(id);
	}

	@Override
	public Original addOriginalExpert(User u, MultipartFile file, MultiValueMap<String, String> params)
			throws FFmpegException {
		File fileSaved = fileUtilsService.saveFile(file);
		Original originalVideo = new Original(FilenameUtils.removeExtension(fileSaved.getName()),
				fileSaved.getAbsolutePath(), u);
		List<Conversion> conversionsVideo = new ArrayList<>();
		for (Entry<String, List<String>> entry : params.entrySet()) {
			switch (entry.getKey()) {
			case "conversionType":
				entry.getValue().forEach(c -> {
					Conversion x = new Conversion(ConversionType.valueOf(c), originalVideo);
					conversionsVideo.add(x);
				});
				break;
			default:
				break;
			}
		}
		originalVideo.setAllConversions(conversionsVideo);
		if (originalVideo.getAllConversions().isEmpty()) {
			logger.l7dlog(Level.ERROR, TRACE_NO_CONVERSION_TYPE_FOUND, new String[] { originalVideo.getName() }, null);
			throw new FFmpegException(FFmpegException.EX_NO_CONVERSION_TYPE_FOUND,
					new String[] { originalVideo.getName() });
		}
		originalVideoRepository.save(originalVideo);
		return originalVideo;
	}

	@Override
	public Original addOriginalBasic(User u, MultipartFile file, List<ConversionType> type)
			throws FFmpegException {
		File fileSaved = fileUtilsService.saveFile(file);
		Original originalVideo = new Original(FilenameUtils.removeExtension(fileSaved.getName()),
				fileSaved.getAbsolutePath(), u);
		List<Conversion> conversionsVideo = new ArrayList<>();

		type.forEach(x -> {
			Conversion y = new Conversion(x, originalVideo);
			conversionsVideo.add(y);
		});
		originalVideo.setAllConversions(conversionsVideo);
		originalVideoRepository.save(originalVideo);
		return originalVideo;
	}

	
}
