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

@Entity
public class School extends Model {

	public static class Builder {
		@Constraints.Required
		private String schoolName;
		private String schoolCity;
		@Constraints.Required
		private String countryCode3;

		public String validate() {
			if (models.Country.getFromCode3(countryCode3) == null) {
				return "Wrong country code";
			} else {
				return null;
			}
		}

		public School generate() {
			Country c = models.Country.getFromCode3(countryCode3);
			School duplicate = School.find.where().eq("Name", schoolName).and().eq("Country", c).and()
					.eq("City", schoolCity).findUnique();
			if (duplicate != null) {
				return duplicate;
			} else {
				return new School(schoolName, c, schoolCity);
			}
		}

		public String getSchoolCity() {
			return schoolCity;
		}

		public void setSchoolCity(String schoolCity) {
			this.schoolCity = schoolCity;
		}

		public String getCountryCode3() {
			return countryCode3;
		}

		public void setCountryCode3(String countryCode3) {
			this.countryCode3 = countryCode3;
		}

		public String getSchoolName() {
			return schoolName;
		}

		public void setSchoolName(String schoolName) {
			this.schoolName = schoolName;
		}

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	@SearcheableField(label = "School", userFetchPath = "User.myEducation.School")
	private String Name;
	@NotNull(message = "Please insert a country")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idcountry")
	private Country Country;
	private String City;

	private School(String name, Country country, String city) {
		Name = name;
		Country = country;
		City = city;
	}

	@Override
	public String toString() {
		return getName() + " (" + getCountry().getCode3() + ")";
	}

	public static Finder<Long, School> find = new Finder<Long, School>(School.class);

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Country getCountry() {
		return Country;
	}

	public void setCountry(Country country) {
		Country = country;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

}