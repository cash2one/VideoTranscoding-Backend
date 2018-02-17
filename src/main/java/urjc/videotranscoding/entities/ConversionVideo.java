package urjc.videotranscoding.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

import urjc.videotranscoding.codecs.ConversionType;

@Entity
@Table(name = "CONVERSION_VIDEO")
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

	@JsonView(Details.class)
	private boolean active;
	/**
	 * 
	 */
	@JsonView(Basic.class)
	private ConversionType conversionType;

	// @ManyToOne(fetch=FetchType.LAZY)
	// @JoinColumn(name="originalVideoId")
	// private OriginalVideo originalVideo;

	protected ConversionVideo() {
	}

	public ConversionVideo(String title, ConversionType conversion, OriginalVideo originalVideo) {
		this.title = title;
		this.conversionType = conversion;
		// this.originalVideo=originalVideo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
