package urjc.videotranscoding.entities;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;

import urjc.videotranscoding.codecs.ConversionType;

@Entity
public class OriginalVideo {
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
	@Enumerated
	private ConversionType listAllConversions;

	protected OriginalVideo() {
	}

	public OriginalVideo(String video) {
		this.originalVideo = video;
	}

	public String getOriginalVideo() {
		return originalVideo;
	}

	public void setOriginalVideo(String originalVideo) {
		this.originalVideo = originalVideo;
	}

	public ConversionType getListAllConversions() {
		return listAllConversions;
	}

	public void setListAllConversions(ConversionType listAllConversions) {
		this.listAllConversions = listAllConversions;
	}
	

}
