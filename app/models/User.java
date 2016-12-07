package models;

import play.data.validation.Constraints;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;

import models.tools.Searchable;

@Entity
public class User extends Model {

	public static class Builder {
		@Constraints.Required
		@Constraints.MinLength(value = 3)
		public String firstName;
		@Constraints.Required
		@Constraints.Email
		public String email;
		@Constraints.Required
		@Constraints.MinLength(value = 5)
		public String password;
		@Constraints.Required
		@Constraints.MinLength(value = 5)
		public String confirmation;

		public String school;
		public String country;

		public String validate() {
			if (User.find.where().eq("email", email).findUnique() != null) {
				return "User already exists";
			}
			if (!password.equals(confirmation)) {
				return "Passwords dont match";
			}
			return null;
		}

		/**
		 * Create and insert a new user according to the mandatory information
		 * input in this class.
		 */
		public User generate() {
			return User.create(email, password, firstName);
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long Id;
	@Constraints.Required
	public String Email;
	@Constraints.Required
	public String Password;
	@Searchable(userFetchPath = "User")
	@Constraints.Required
	public String FirstName;

	// Attributes below are not mandatory
	public String PictureExtension;
	public String IntroductionText;
	public String AppointmentPrice;

	public Integer RatingResult;
	public Timestamp DateRegistration;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IdNationality")
	public Country Nationality;

	@OneToMany(mappedBy = "User", fetch = FetchType.LAZY)
	public List<Education> myEducation;
	@OneToMany(mappedBy = "User")
	public List<WorkCursus> myWorkcursus;
	@OneToMany(mappedBy = "User")
	public List<Experience> myExperience;
	@OneToMany(mappedBy = "User")
	public List<Funding> myFunding;
	@OneToMany(mappedBy = "User")
	public List<Contact> myContactsInfo;

	public static Finder<Long, User> find = new Finder<Long, User>(User.class);

	/**
	 * If the password and email match an entry in the database, the user is
	 * returned. Null otherwise.
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public static User authenticate(String email, String password) {
		return find.where().eq("email", email).eq("password", password).findUnique();
	}

	/**
	 * Create a new {@link User} with the mandatory information
	 * 
	 * @param email
	 * @param password
	 * @param firstName
	 */
	protected User(String email, String password, String firstName) {
		Email = email;
		Password = password;
		FirstName = firstName;
	}

	/**
	 * Create a {@linkplain User } and save it in the database.
	 * 
	 * @param email
	 * @param password
	 * @param firstName
	 * @return
	 */
	public static User create(String email, String password, String firstName) {
		User u = new User(email, password, firstName);
		u.insert();
		return u;
	}

	@Override
	public String toString() {
		return "User [Id=" + Id + ", FirstName=" + FirstName + "]";
	}

}