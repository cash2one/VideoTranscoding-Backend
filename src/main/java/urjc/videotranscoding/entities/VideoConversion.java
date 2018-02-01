package urjc.videotranscoding.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

import urjc.videotranscoding.codecs.ConversionType;

/**
 * @author luisca
 * @since 0.5
 */
@Entity
@Table(name = "VideoConversion")
public class VideoConversion {
	public interface Basic {
	}

	public interface Details {
	}
	/**
	 * VideoConversion id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Basic.class)
	private long videoConversionId;
	/**
	 * Path where localized the original Video
	 */
	private String originalVideo;
	/**
	 * 
	 */
	private final Set<Tripla<Boolean, ConversionType, String>> listConversions= new HashSet<>();

	/**
	 * Constructor for Hibernate
	 */
	protected VideoConversion() {
	}

	public String getOriginalVideo() {
		return originalVideo;
	}

	public void setOriginalVideo(String originalVideo) {
		this.originalVideo = originalVideo;
	}

	public Set<Tripla<Boolean, ConversionType, String>> getListConversions() {
		return listConversions;
	}

	public void addToListConversions(
			Tripla<Boolean, ConversionType, String> listConversions) {
		this.listConversions.add(listConversions);
	}

}
