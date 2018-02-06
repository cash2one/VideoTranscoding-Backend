package urjc.videotranscoding.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
	 * List of All Conversions for the video
	 */
	@JsonView(Basic.class)
	private String originalVideo;
	@JsonView(Details.class)
	@ElementCollection(targetClass = ConversionType.class)
	@Column(name = "listAllConversions")
	@Enumerated
	private Collection<ConversionType> listAllConversions = new ArrayList<>();
	/**
	 * If is complete true, EOC false
	 */
	@JsonView(Details.class)
	private boolean complete;

	

	protected OriginalVideo() {
	}

	public OriginalVideo(String video,boolean complete) {
		this.originalVideo = video;
		this.complete=complete;
	}

	public String getOriginalVideo() {
		return originalVideo;
	}

	public void setOriginalVideo(String originalVideo) {
		this.originalVideo = originalVideo;
	}

	public Collection<ConversionType> getListAllConversions() {
		return listAllConversions;
	}

	public void setListAllConversions(
			Collection<ConversionType> allConversions) {
		this.listAllConversions = allConversions;
	}
	public void addConversion(ConversionType conversion) {
		this.listAllConversions.add(conversion);
	}

}
