package urjc.videotranscoding.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "USER")
public class User {
	public interface Basic {
	}
	public interface Details {
	}

	private final int WORKLOAD = 12;

	/**
	 * User id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Basic.class)
	private long userId;

	/**
	 * User email
	 */
	@JsonView(Basic.class)
	@Column(unique = true)
	private String email;

	/**
	 * User nick
	 */
	@JsonView(Basic.class)
	@Column(unique = true)
	private String nick;

	/**
	 * User Password
	 */
	@JsonIgnore
	private String userPassword;

	/**
	 * User photo
	 */
	@JsonView(Basic.class)
	private String photo = "https://placeimg.com/180/180/any";

	/**
	 * User Roles
	 */
	@JsonView(Details.class)
	@ElementCollection(fetch = FetchType.EAGER)
	private final Set<UserRoles> roles = new HashSet<>();

	// @JsonView(Details.class)
	// @ElementCollection
	// @Column(name = "Favourite Categories")
	// private final Set<String> favouriteCategories = new HashSet<>();

	@JsonView(Details.class)
	@Column(name = "List Videos")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private final Map<String, OriginalVideo> listVideos = new HashMap<>();

	protected User() {
	}

	public User(String email, String nick, String userPassword, String photo,
			UserRoles... roles) {
		this.email = email;
		this.nick = nick;
		String salt = BCrypt.gensalt(WORKLOAD);
		this.userPassword = BCrypt.hashpw(userPassword, salt);
		this.photo = (photo != null && photo != "") ? photo : this.photo;
		this.roles.addAll(Arrays.asList(roles));
	}

	public long getUserId() {
		return userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNick() {
		return nick;
	}

	public String getHashedPassword() {
		return userPassword;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public void addRole(UserRoles rol) {
		this.roles.add(rol);
	}

	public void removeRole(UserRoles rol) {
		this.roles.remove(rol);
	}

	public Collection<UserRoles> getRoles() {
		return new ArrayList<>(this.roles);
	}

	public boolean isAdmin() {
		return this.roles.contains(UserRoles.ADMIN);
	}

	public boolean isUser() {
		return this.roles.contains(UserRoles.USER);
	}

	public boolean isValidPassword(String password) {
		return BCrypt.checkpw(password, userPassword);
	}

	public void changePassword(String newPassword) {
		userPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(WORKLOAD));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof User))
			return false;
		User user = (User) o;
		return userId == user.userId;
	}

	@Override
	public int hashCode() {
		return (int) (userId ^ (userId >>> 32));
	}

	public void addVideo(OriginalVideo newVideo) {
		this.listVideos.put(newVideo.getOriginalVideo(), newVideo);
	}

}
