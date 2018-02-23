package es.urjc.videotranscoding.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author luisca
 * @since 0.5
 */
//@Entity
//@Table(name = "VideoConversion_Expert")
public class VideoConversionExpert {
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
	 * Path where is localized the original Video
	 */
	@JsonView(Basic.class)
	private String originalVideo;
	/**
	 * 
	 */
	//private final Set<Tripla<Boolean, ConversionType, String>> listConversions = new HashSet<>();

	/**
	 * Constructor for Hibernate
	 */
	protected VideoConversionExpert() {
	}

	public String getOriginalVideo() {
		return originalVideo;
	}

	public void setOriginalVideo(String originalVideo) {
		this.originalVideo = originalVideo;
	}

//	public Set<Tripla<Boolean, ConversionType, String>> getListConversions() {
//		return listConversions;
//	}
//
//	public void addToListConversions(Tripla<Boolean, ConversionType, String> listConversions) {
//		this.listConversions.add(listConversions);
//	}

}
