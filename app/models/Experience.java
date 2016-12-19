package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.avaje.ebean.Model;

import models.tools.SearcheableField;
import models.tools.UserBelonging;

@Entity
public class Experience extends Model implements UserBelonging {

	public static class Builder {

		@Constraints.Required
		private String Name;
		private String Duration;
		private String Details;

		public String validate() {
			return null;
		}

		public Experience generate(User user) {
			return new Experience(user, Name, Duration, Details);
		}

		public String getName() {
			return Name;
		}

		public void setName(String name) {
			Name = name;
		}

		public String getDuration() {
			return Duration;
		}

		public void setDuration(String duration) {
			Duration = duration;
		}

		public String getDetails() {
			return Details;
		}

		public void setDetails(String details) {
			Details = details;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IdUser")
	@NotNull
	private User User;
	@SearcheableField(label = "Experience", userFetchPath = "User.myExperience")
	private String Name;
	private String Duration;
	private String Details;

	public static Finder<Long, Experience> find = new Finder<Long, Experience>(Experience.class);

	private Experience(models.User user, String name, String duration, String details) {
		User = user;
		Name = name;
		Duration = duration;
		Details = details;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public User getUser() {
		return User;
	}

	public void setUser(User user) {
		User = user;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDuration() {
		return Duration;
	}

	public void setDuration(String duration) {
		Duration = duration;
	}

	public String getDetails() {
		return Details;
	}

	public void setDetails(String details) {
		Details = details;
	}

	@Override
	public boolean hasRight(models.User user) {
		return this.User.getId().equals(user.getId());
	}

}
