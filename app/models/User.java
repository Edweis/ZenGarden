package models;

import play.data.validation.Constraints;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;

import models.tools.SearcheableField;

@Entity
public class User extends Model {

	private static final String APPLICATION_PATH = "/media/piou/Data/Documents/Developpement/Banquise/green";
	private static final String ASSETS_PATH = "/public";
	private static final String PICTURE_PATH = "/images/profile/";
	private static final String DEFAULT_PICTURE_PATH = "default.jpg";

	/**
	 * Class that is used to create a user through a form.
	 * 
	 * @author piou
	 *
	 */
	public static class Builder {
		@Constraints.Required
		@Constraints.MinLength(value = 3)
		private String firstName;
		@Constraints.Required
		@Constraints.Email
		private String email;
		@Constraints.Required
		@Constraints.MinLength(value = 5)
		private String password;
		@Constraints.Required
		@Constraints.MinLength(value = 5)
		private String confirmation;

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

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getConfirmation() {
			return confirmation;
		}

		public void setConfirmation(String confirmation) {
			this.confirmation = confirmation;
		}

	}

	/**
	 * Update values of the model from a form.
	 * 
	 * @author piou
	 *
	 */
	public static class Updater {

		private String PictureExtension;
		private String IntroductionText;
		private String AppointmentPrice;
		private String Nationality;

		public String validate() {
			if (Nationality != null) {
				if (Country.getFromCode3(Nationality) == null) {
					return "wrong country code";
				}
			}

			return null;
		}

		public User update(User user) {
			if (PictureExtension != null) {
				user.setPictureExtension(PictureExtension);
			}
			if (IntroductionText != null) {
				user.setIntroductionText(IntroductionText);
			}
			if (AppointmentPrice != null) {
				user.setAppointmentPrice(AppointmentPrice);
			}
			if (Nationality != null) {
				user.setNationality(Country.getFromCode3(Nationality));
			}

			return user;
		}

		public String getIntroductionText() {
			return IntroductionText;
		}

		public void setIntroductionText(String introductionText) {
			IntroductionText = introductionText;
		}

		public String getAppointmentPrice() {
			return AppointmentPrice;
		}

		public void setAppointmentPrice(String appointmentPrice) {
			AppointmentPrice = appointmentPrice;
		}

		public String getNationality() {
			return Nationality;
		}

		public void setNationality(String nationality) {
			Nationality = nationality;
		}

		public String displayResult() {
			if (this.IntroductionText != null) {
				return this.IntroductionText;
			}
			if (this.AppointmentPrice != null) {
				return this.AppointmentPrice;
			}
			if (this.Nationality != null) {
				return this.Nationality.toString();
			}
			return "Error in User.Updater.displayResult, no data to display.";
		}

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	@Constraints.Required
	private String Email;
	@Constraints.Required
	private String Password;
	@SearcheableField(label = "Name", userFetchPath = "User")
	@Constraints.Required
	private String FirstName;

	// Attributes below are not mandatory
	private String PictureExtension;
	private String IntroductionText;
	private String AppointmentPrice;

	private Integer RatingResult;
	private Timestamp DateRegistration;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IdNationality")
	private Country Nationality;

	@OneToMany(mappedBy = "User", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<Education> myEducation;
	@OneToMany(mappedBy = "User", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<WorkCursus> myWorkcursus;
	@OneToMany(mappedBy = "User", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<Experience> myExperience;
	@OneToMany(mappedBy = "User", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<Funding> myFunding;
	@OneToMany(mappedBy = "User", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<Contact> myContactsInfo;

	@OneToMany(mappedBy = "HasShared", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<Relationship> myRelationships;

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

		// set email as contact information. I use ci variable because I have a
		// NullPointerExecption when I work directly on myContactInfo
		ArrayList<Contact> ci = new ArrayList<Contact>();
		models.Contact.Builder cb = new Contact.Builder();
		cb.setType("Email");
		cb.setValue(email);
		ci.add(cb.generate(this));
		myContactsInfo = ci;
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

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		if (((User) obj).getId().equals(this.getId())) {
			return true;
		} else {
			return false;
		}
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getPictureExtension() {
		if (this.PictureExtension == null) {
			return PICTURE_PATH + DEFAULT_PICTURE_PATH;
		} else {
			return PictureExtension;
		}
	}

	public void setPictureExtension(String pictureExtension) {
		PictureExtension = pictureExtension;
	}

	public String getIntroductionText() {
		return IntroductionText;
	}

	public void setIntroductionText(String introductionText) {
		IntroductionText = introductionText;
	}

	public String getAppointmentPrice() {
		return AppointmentPrice;
	}

	public void setAppointmentPrice(String appointmentPrice) {
		AppointmentPrice = appointmentPrice;
	}

	public Timestamp getDateRegistration() {
		return DateRegistration;
	}

	public void setDateRegistration(Timestamp dateRegistration) {
		DateRegistration = dateRegistration;
	}

	public Country getNationality() {
		return Nationality;
	}

	public void setNationality(Country nationality) {
		Nationality = nationality;
	}

	public List<Education> getMyEducation() {
		return myEducation;
	}

	public void setMyEducation(List<Education> myEducation) {
		this.myEducation = myEducation;
	}

	public List<WorkCursus> getMyWorkcursus() {
		return myWorkcursus;
	}

	public void setMyWorkcursus(List<WorkCursus> myWorkcursus) {
		this.myWorkcursus = myWorkcursus;
	}

	public List<Experience> getMyExperience() {
		return myExperience;
	}

	public void setMyExperience(List<Experience> myExperience) {
		this.myExperience = myExperience;
	}

	public List<Funding> getMyFunding() {
		return myFunding;
	}

	public void setMyFunding(List<Funding> myFunding) {
		this.myFunding = myFunding;
	}

	public List<Contact> getMyContactsInfo() {
		return myContactsInfo;
	}

	public void setMyContactsInfo(List<Contact> myContactsInfo) {
		this.myContactsInfo = myContactsInfo;
	}

	public Integer getRatingResult() {
		return RatingResult;
	}

	public List<Relationship> getMyRelationships() {
		return myRelationships;
	}

	public void setMyRelationships(List<Relationship> myRelationships) {
		this.myRelationships = myRelationships;
	}

	public void setRatingResult(Integer ratingResult) {
		RatingResult = ratingResult;
	}

	/**
	 * Generate the path and the name of the picture with the extension
	 * 
	 * @param extension
	 * @param connectedUser
	 * @return the path and the name of the picture without the extension
	 */
	public static String generateAbsolutePicturePath(User user, String extension) {
		return APPLICATION_PATH + ASSETS_PATH + generateRelativePicturePath(user, extension);
	}

	public static String generateFileName(User user, String extension) {
		return user.getId() + "pp" + "." + extension;
	}

	public static String generateRelativePicturePath(User user, String extension) {
		return PICTURE_PATH + generateFileName(user, extension);
	}

}