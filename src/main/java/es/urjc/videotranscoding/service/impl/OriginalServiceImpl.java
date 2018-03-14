package es.urjc.videotranscoding.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import es.urjc.videotranscoding.codecs.ConversionType;
import es.urjc.videotranscoding.codecs.ConversionTypeBasic;
import es.urjc.videotranscoding.codecs.ConversionTypeBasic.Types;
import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.exception.FFmpegException;
import es.urjc.videotranscoding.repository.OriginalRepository;
import es.urjc.videotranscoding.service.ConversionService;
import es.urjc.videotranscoding.service.OriginalService;
import es.urjc.videotranscoding.service.UserService;
import es.urjc.videotranscoding.wrapper.FfmpegResourceBundle;

@Service
public class OriginalServiceImpl implements OriginalService {
	private static final Logger logger = Logger.getLogger(OriginalServiceImpl.class);
	private static final String FICH_TRAZAS = "fichero.mensajes.trazas";
	private static final String TRACE_NO_CONVERSION_TYPE_FOUND = "ffmpeg.conversionType.notFound";
	private static final String TRACE_ILEGAL_ARGUMENT = "ffmpeg.argument.notFound";
	@Autowired
	private OriginalRepository originalVideoRepository;

	@Autowired
	private ConversionService conversionService;
	@Autowired
	private UserService userService;
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

	public User deleteOriginal(Original video, User u) {
		// TODO DELETES....
		if (u.isAdmin()) {
			fileUtilsService.deleteFile(video.getPath());
			List<Conversion> listToRemove = new ArrayList<>();
			video.getAllConversions().forEach(x -> listToRemove.add(x));
			listToRemove.forEach(conversion -> conversionService.deleteConversion(conversion, u));
			u.removeVideo(video);
			userService.save(u);
			originalVideoRepository.deleteById(video.getOriginalId());
		} else {
			for (Original iterator : u.getListVideos()) {
				if (iterator.getOriginalId().equals(video.getOriginalId())) {
					iterator.getAllConversions().forEach(x -> conversionService.deleteConversion(x, u));
					fileUtilsService.deleteFile(video.getPath());
					originalVideoRepository.delete(iterator);
				}
			}
		}
		return u;
	}

	public User deleteAllVideos(User u) {
		u.removeAllVideos();
		userService.save(u);
		originalVideoRepository.deleteAll();
		return u;
	}

	public List<Original> findAllVideos() {
		return originalVideoRepository.findAll();
	}

	public Optional<Original> findOneVideo(long id) {
		return originalVideoRepository.findById(id);
	}

	@Transactional(rollbackFor = FFmpegException.class)
	public Original addOriginalExpert(User u, MultipartFile file, List<String> params) throws FFmpegException {
		File fileSaved = null;
		try {
			fileSaved = fileUtilsService.saveFile(file);
			Original originalVideo = new Original(FilenameUtils.removeExtension(fileSaved.getName()),
					fileSaved.getAbsolutePath(), u);
			List<Conversion> conversionsVideo = new ArrayList<>();
			List<ConversionType> listConversion = new ArrayList<>();
			params.forEach(x -> {
				try {
					listConversion.add(ConversionType.valueOf(x));
				} catch (IllegalArgumentException e) {
					logger.l7dlog(Level.WARN, TRACE_ILEGAL_ARGUMENT, new String[] { x }, null);
				}
			});
			if (listConversion.isEmpty()) {
				throw new FFmpegException(FFmpegException.EX_NO_CONVERSION_TYPE_FOUND,
						new String[] { originalVideo.getName() });
			}
			listConversion.forEach(d -> {
				Conversion x = new Conversion(d, originalVideo);
				conversionsVideo.add(x);
			});
			originalVideo.setAllConversions(conversionsVideo);
			if (originalVideo.getAllConversions().isEmpty()) {
				logger.l7dlog(Level.ERROR, TRACE_NO_CONVERSION_TYPE_FOUND, new String[] { originalVideo.getName() },
						null);
				throw new FFmpegException(FFmpegException.EX_NO_CONVERSION_TYPE_FOUND,
						new String[] { originalVideo.getName() });
			}
			originalVideoRepository.save(originalVideo);
			return originalVideo;
		} catch (FFmpegException e) {
			fileUtilsService.deleteFile(fileSaved.getAbsolutePath());
			throw e;
		}
	}

	@Transactional(rollbackFor = FFmpegException.class)
	public Original addOriginalBasic(User u, MultipartFile file, List<String> params) throws FFmpegException {
		File fileSaved = null;
		try {
			fileSaved = fileUtilsService.saveFile(file);
			Original originalVideo = new Original(FilenameUtils.removeExtension(fileSaved.getName()),
					fileSaved.getAbsolutePath(), u);

			Set<ConversionType> listConversion = new HashSet<>();
			params.forEach(x -> {
				try {
					listConversion.addAll(ConversionTypeBasic.getConversion(Enum.valueOf(Types.class, x)));
				} catch (IllegalArgumentException e) {
					logger.l7dlog(Level.WARN, TRACE_ILEGAL_ARGUMENT, new String[] { x }, null);
				}
			});

			if (listConversion.isEmpty()) {
				throw new FFmpegException(FFmpegException.EX_NO_CONVERSION_TYPE_FOUND,
						new String[] { originalVideo.getName() });
			}
			List<Conversion> conversionsVideo = new ArrayList<>();

			listConversion.forEach(x -> {
				Conversion y = new Conversion(x, originalVideo);
				conversionsVideo.add(y);
			});
			originalVideo.setAllConversions(conversionsVideo);
			originalVideoRepository.save(originalVideo);
			return originalVideo;
		} catch (FFmpegException e) {
			fileUtilsService.deleteFile(fileSaved.getAbsolutePath());
			throw e;
		}
	}

}
