package es.urjc.videotranscoding.service;

import java.util.Optional;

import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.entities.User;

public interface ConversionService {
	/**
	 * Save the conversion on the BBDD.
	 * 
	 * @param video
	 *            The video that you will save
	 */
	void save(Conversion video);

	/**
	 * Delete a conversion and her original delete it too.
	 * 
	 * @param original
	 *            Video original for delete a conversion.
	 * @param conversion
	 *            that you want delete
	 * @param u
	 *            User that contain the video. It`s necessary for save the entity
	 * @return user updated.
	 */
	User deleteConversion(Original original, Conversion conversion, User u);

	/**
	 * Find a conversion and can return the conversion or no.
	 * 
	 * @param id
	 *            for search the conversion
	 * @return An optional with a conversion
	 */
	Optional<Conversion> findOneConversion(long id);
}
