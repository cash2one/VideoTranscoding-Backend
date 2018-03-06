package es.urjc.videotranscoding.service;

import es.urjc.videotranscoding.entities.Conversion;

import java.util.Optional;

public interface ConversionService {

    void save(Conversion video);

    void delete(Conversion video);

    void delete(long id);

    Optional<Conversion> findOneConversion(long id);
}
