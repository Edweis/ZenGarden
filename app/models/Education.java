package models;

import play.data.validation.Constraints;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Pattern;

import com.avaje.ebean.Model;

import models.tools.Searchable;
import models.tools.UserBelonging;

@Entity
public class Education extends Model implements UserBelonging {

	public static class Builder {
		@Constraints.Required()
		private Integer DurationMonth;
		private Integer StartYear;
		private String Promotion;
		@Constraints.Required
		private String Major;
		private Boolean IsHomeUniversity;
		private Boolean IsCurrentEducation;

		public Education generate(User user, School school) {
			return new Education(user, school, (DurationMonth), (StartYear), Promotion, Major, (IsHomeUniversity),
					(IsCurrentEducation));
		}

		public Education replace(Education e) {
			e.DurationMonth = (DurationMonth);
			e.StartYear = (this.StartYear);
			e.Promotion = this.Promotion;
			e.Major = this.Major;
			e.IsHomeUniversity = (this.IsHomeUniversity);
			e.IsCurrentEducation = (this.IsCurrentEducation);
			return e;
		}

		public Integer getDurationMonth() {
			return DurationMonth;
		}

		public void setDurationMonth(Integer durationMonth) {
			DurationMonth = durationMonth;
		}

		public Integer getStartYear() {
			return StartYear;
		}

		public void setStartYear(Integer startYear) {
			StartYear = startYear;
		}

		public String getPromotion() {
			return Promotion;
		}

		public void setPromotion(String promotion) {
			Promotion = promotion;
		}

		public String getMajor() {
			return Major;
		}

		public void setMajor(String major) {
			Major = major;
		}

		public Boolean getIsHomeUniversity() {
			return IsHomeUniversity;
		}

		public void setIsHomeUniversity(Boolean isHomeUniversity) {
			IsHomeUniversity = isHomeUniversity;
		}

		public Boolean getIsCurrentEducation() {
			return IsCurrentEducation;
		}

		public void setIsCurrentEducation(Boolean isCurrentEducation) {
			IsCurrentEducation = isCurrentEducation;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iduser")
	private User User;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "idschool")
	private School School;
	private Integer DurationMonth;
	private Integer StartYear;
	private String Promotion;
	@Searchable(userFetchPath = "User.myEducation")
	private String Major;
	private Boolean IsHomeUniversity;
	private Boolean IsCurrentEducation;
	@Pattern(regexp = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)", message = "Please select a valide mail address")
	private String VerifEmail;
	private Boolean IsEmailVerified;

	public static Finder<Long, Education> find = new Finder<Long, Education>(Education.class);

	protected Education(models.User user, models.School school, Integer durationMonth, Integer startYear,
			String promotion, String major, Boolean isHomeUniversity, Boolean isCurrentEducation) {
		User = user;
		School = school;
		DurationMonth = durationMonth;
		StartYear = startYear;
		Promotion = promotion;
		Major = major;
		IsHomeUniversity = isHomeUniversity;
		IsCurrentEducation = isCurrentEducation;
	}

	@Override
	public String toString() {
		return "Education [Id=" + Id + ", User=" + User + ", School=" + School + ", DurationMonth=" + DurationMonth
				+ ", StartYear=" + StartYear + ", Promotion=" + Promotion + ", Major=" + Major + ", IsHomeUniversity="
				+ IsHomeUniversity + ", IsCurrentEducation=" + IsCurrentEducation + ", VerifEmail=" + VerifEmail
				+ ", IsEmailVerified=" + IsEmailVerified + "]";
	}

	@Override
	public boolean hasRight(models.User user) {
		return this.User.Id.equals(user.Id);
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

	public School getSchool() {
		return School;
	}

	public void setSchool(School school) {
		School = school;
	}

	public Integer getDurationMonth() {
		return DurationMonth;
	}

	public void setDurationMonth(Integer durationMonth) {
		DurationMonth = durationMonth;
	}

	public Integer getStartYear() {
		return StartYear;
	}

	public void setStartYear(Integer startYear) {
		StartYear = startYear;
	}

	public String getPromotion() {
		return Promotion;
	}

	public void setPromotion(String promotion) {
		Promotion = promotion;
	}

	public String getMajor() {
		return Major;
	}

	public void setMajor(String major) {
		Major = major;
	}

	public Boolean getIsHomeUniversity() {
		return IsHomeUniversity;
	}

	public void setIsHomeUniversity(Boolean isHomeUniversity) {
		IsHomeUniversity = isHomeUniversity;
	}

	public Boolean getIsCurrentEducation() {
		return IsCurrentEducation;
	}

	public void setIsCurrentEducation(Boolean isCurrentEducation) {
		IsCurrentEducation = isCurrentEducation;
	}

	public String getVerifEmail() {
		return VerifEmail;
	}

	public void setVerifEmail(String verifEmail) {
		VerifEmail = verifEmail;
	}

	public Boolean getIsEmailVerified() {
		return IsEmailVerified;
	}

	public void setIsEmailVerified(Boolean isEmailVerified) {
		IsEmailVerified = isEmailVerified;
	}

}