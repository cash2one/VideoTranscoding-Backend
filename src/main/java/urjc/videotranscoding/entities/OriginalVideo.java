package urjc.videotranscoding.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "ORIGINAL_VIDEO")
public class OriginalVideo {
	public interface Basic {
	}

	public interface Details {
	}

	public interface None {
	}

	/**
	 * VideoConversion id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Basic.class)
	private long originalVideoId;
	/**
	 * List of All Conversions for the video
	 */
	@JsonView(Basic.class)
	private String originalVideo;
	/**
	 * 
	 */
	@JsonView(None.class)
	@ManyToOne
	private User userVideo;

	/**
	 * 
	 */
	// @JsonView(Details.class)
	// @ElementCollection(targetClass = ConversionType.class)
	// @Column(name = "listAllConversions")
	// @Enumerated
	// private Collection<ConversionType> listAllConversions = new
	// ArrayList<>();
	/**
	 * 
	 */

	@JsonView(Details.class)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	private List<ConversionVideo> allConversions = new ArrayList<>();

	/**
	 * If is complete true, EOC false
	 */
	@JsonView(Details.class)
	private boolean complete;
	/**
	 * If any conversion is active this is active;
	 */
	@JsonView(Details.class)
	private boolean active;

	protected OriginalVideo() {
	}

	public OriginalVideo(String video, boolean complete, User u) {
		this.originalVideo = video;
		this.complete = complete;
		this.userVideo = u;
	}

	public String getOriginalVideo() {
		return originalVideo;
	}

	public void setOriginalVideo(String originalVideo) {
		this.originalVideo = originalVideo;
	}

	public boolean isComplete() {
		return complete;
	}

	public List<ConversionVideo> getAllConversions() {
		return allConversions;
	}

	public void setAllConversions(List<ConversionVideo> allConversions) {
		this.allConversions = allConversions;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public User getUserVideo() {
		return userVideo;
	}

	public void setUserVideo(User userVideo) {
		this.userVideo = userVideo;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	

}
