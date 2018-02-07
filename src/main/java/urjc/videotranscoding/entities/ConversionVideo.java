package urjc.videotranscoding.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

import urjc.videotranscoding.codecs.ConversionType;

@Entity
@Table(name = "Conversion_Video")
public class ConversionVideo {
	public interface Basic {
	}

	public interface Details {
	}
	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Basic.class)
	private long conversionId;
	/**
	 * 
	 */
	@JsonView(Basic.class)
	private String title;
	/**
	 * 
	 */
	@JsonView(Details.class)
	private String progress;
	/**
	 * 
	 */
	@JsonView(Details.class)
	private boolean finished;
	/**
	 * 
	 */
	@JsonView(Basic.class)
	private ConversionType conversionType;

	@ManyToOne(fetch = FetchType.EAGER)
	private OriginalVideo originalVideo;

	protected ConversionVideo() {
	}
	public ConversionVideo(String title, ConversionType conversion,OriginalVideo originalVideo) {
		this.title = title;
		this.conversionType = conversion;
		this.originalVideo=originalVideo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}
	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	public ConversionType getConversionType() {
		return conversionType;
	}
	public void setConversionType(ConversionType conversionType) {
		this.conversionType = conversionType;
	}
	public OriginalVideo getOriginalVideo() {
		return originalVideo;
	}
	public void setOriginalVideo(OriginalVideo originalVideo) {
		this.originalVideo = originalVideo;
	}

}
