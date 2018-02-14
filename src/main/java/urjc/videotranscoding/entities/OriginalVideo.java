package urjc.videotranscoding.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
	//@JsonView(None.class)
	//@ManyToOne
	//private User userVideo;

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

	//@JsonView(Details.class)
	@OneToMany(mappedBy = "originalVideo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<ConversionVideo> allConversions = new HashSet<>();
	/**
	 * If is complete true, EOC false
	 */
	@JsonView(Details.class)
	private boolean complete;

	protected OriginalVideo() {
	}

	public OriginalVideo(String video, boolean complete, User u) {
		this.originalVideo = video;
		this.complete = complete;
	}

	public String getOriginalVideo() {
		return originalVideo;
	}

	public void setOriginalVideo(String originalVideo) {
		this.originalVideo = originalVideo;
	}

	// public Collection<ConversionType> getListAllConversions() {
	// return listAllConversions;
	// }
	//
	// public void setListAllConversions(
	// Collection<ConversionType> allConversions) {
	// this.listAllConversions = allConversions;
	// }
	// public void addConversion(ConversionType conversion) {
	// this.listAllConversions.add(conversion);
	// }

	public boolean isComplete() {
		return complete;
	}

	public Set<ConversionVideo> getAllConversions() {
		return allConversions;
	}

	public void setAllConversions(Set<ConversionVideo> allConversions) {
		this.allConversions = allConversions;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

//	public User getUserVideo() {
//		return userVideo;
//	}
//
//	public void setUserVideo(User userVideo) {
//		this.userVideo = userVideo;
//	}

}
