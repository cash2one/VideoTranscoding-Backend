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
import es.urjc.videotranscoding.entities.ConversionVideo;
import es.urjc.videotranscoding.entities.OriginalVideo;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.exception.FFmpegException;
import es.urjc.videotranscoding.repository.OriginalVideoRepository;
import es.urjc.videotranscoding.service.FileService;
import es.urjc.videotranscoding.service.OriginalVideoService;
import es.urjc.videotranscoding.wrapper.FfmpegResourceBundle;

@Service
public class OriginalVideoServiceImpl implements OriginalVideoService {
	private static final Logger logger = Logger.getLogger(OriginalVideoServiceImpl.class);
	private static final String FICH_TRAZAS = "fichero.mensajes.trazas";
	private static final String TRACE_NO_CONVERSION_TYPE_FOUND = "ffmpeg.conversionType.notFound";
	@Autowired
	private OriginalVideoRepository originalVideoRepository;
	@Autowired
	private FileService fileService;

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

	public void save(OriginalVideo video) {
		originalVideoRepository.save(video);
	}

	public void delete(OriginalVideo video) {
		originalVideoRepository.delete(video);
	}

	public void delete(long id) {
		originalVideoRepository.deleteById(id);
	}

	public List<OriginalVideo> findAllVideos() {
		return originalVideoRepository.findAll();
	}

	public Optional<OriginalVideo> findOneVideo(long id) {
		return originalVideoRepository.findById(id);
	}

	@Override
	public OriginalVideo addOriginalVideoExpert(User u, MultipartFile file, MultiValueMap<String, String> params)
			throws FFmpegException {
		File fileSaved = fileService.saveFile(file);
		OriginalVideo originalVideo = new OriginalVideo(FilenameUtils.removeExtension(fileSaved.getName()),
				fileSaved.getAbsolutePath(), u);
		List<ConversionVideo> conversionsVideo = new ArrayList<>();
		for (Entry<String, List<String>> entry : params.entrySet()) {
			switch (entry.getKey()) {
			case "conversion":
				entry.getValue().forEach(c -> {
					ConversionVideo x = new ConversionVideo(ConversionType.valueOf(c), originalVideo);
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
	public OriginalVideo addOriginalVideoBasic(User u, MultipartFile file, List<ConversionType> type)
			throws FFmpegException {
		File fileSaved = fileService.saveFile(file);
		OriginalVideo originalVideo = new OriginalVideo(FilenameUtils.removeExtension(fileSaved.getName()),
				fileSaved.getAbsolutePath(), u);
		List<ConversionVideo> conversionsVideo = new ArrayList<>();

		type.forEach(x -> {
			ConversionVideo y = new ConversionVideo(x, originalVideo);
			conversionsVideo.add(y);
		});
		originalVideo.setAllConversions(conversionsVideo);
		originalVideoRepository.save(originalVideo);
		return originalVideo;
	}
}
