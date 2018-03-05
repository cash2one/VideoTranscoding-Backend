package es.urjc.videotranscoding.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name = "ORIGINAL")
public class Original {
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
	private long originalId;
	/**
	 * Name of the video
	 */
	@JsonView(Basic.class)
	@Column(unique = true)
	private String name;
	/**
	 * Path of the video
	 */
	@Column(unique = true)
	@JsonView(Details.class)
	private String path;
	/**
	 * User of the video
	 */
	@JsonView(None.class)
	@ManyToOne
	private User userVideo;

	/**
	 * All Conversions of the video
	 */
	@JsonView(Basic.class)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	private List<Conversion> conversions = new ArrayList<>();

	/**
	 * If all conversions are complete true, EOC false
	 */
	@JsonView(Details.class)
	private boolean complete;
	/**
	 * If any conversion is active this is active;
	 */
	@JsonView(Details.class)
	private boolean active;

	/**
	 * Constructor protected for hibernate.
	 */
	protected Original() {
	}

	/**
	 * Constructor for the Original vide
	 * 
	 * @param video
	 *            name of the video
	 * @param path
	 *            of the video
	 * @param user
	 *            user for the video
	 */
	public Original(String name, String path, User user) {
		this.name = name.replace(" ", "_");
		this.path = path;
		this.userVideo = user;

	}

	public boolean isComplete() {
		for (Conversion conversionVideo : conversions) {
			if (!conversionVideo.isFinished()) {
				return false;
			}
		}
		return true;
	}

	public boolean isActive() {
		for (Conversion conversionVideo : conversions) {
			if (conversionVideo.isActive()) {
				return true;
			}
		}
		return false;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<Conversion> getAllConversions() {
		return conversions;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setAllConversions(List<Conversion> conversions) {
		this.conversions = conversions;
	}

}
