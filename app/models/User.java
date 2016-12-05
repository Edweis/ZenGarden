package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;

import models.annotations.Searchable;

@Entity
public class User extends Model {

	@Id
	public Long Id;
	@Constraints.Required
	public String Email;
	@Constraints.Required
	public String Password;
	@Searchable(userFetchPath = "User")
	@Constraints.Required
	public String FirstName;

	// Attributes below are not mandatory
	// public String PictureExtension;
	// public String IntroductionText;
	// public String AppointmentPrice;
	//
	// public Integer RatingResult;
	// @Formats.DateTime(pattern="dd/MM/yy")
	// public Date DateRegistration;
	// @JoinColumn(name = "IdNationality")
	// public Country Nationality;
	//
	// @OneToMany(mappedBy = "User")
	// public List<Education> myEducation;
	// @OneToMany(mappedBy = "User")
	// public List<WorkCursus> myWorkcursus;
	// @OneToMany(mappedBy = "User")
	// public List<Experience> myExperience;
	// @OneToMany(mappedBy = "User")
	// public List<Funding> myFunding;
	// @OneToMany(mappedBy = "User")
	// public List<Contact> myContactsInfo;

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
}