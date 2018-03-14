package es.urjc.videotranscoding.service;

import java.util.Optional;

import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.entities.User;

public interface ConversionService {

	void save(Conversion video);

	User deleteConversion(Conversion video, User u);

	Optional<Conversion> findOneConversion(long id);
}
