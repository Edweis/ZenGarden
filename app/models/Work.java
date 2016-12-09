package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

import models.tools.SearcheableField;

@Entity
public class Work extends Model {

	public static class Builder {

		@Constraints.Required
		private String CompanyName;
		@Constraints.Required
		private String countryCode3;
		private String City;

		public String validate() {
			if (models.Country.getFromCode3(countryCode3) == null) {
				return "Wrong country code";
			} else {
				return null;
			}
		}

		public Work generate() {
			return new Work(CompanyName, models.Country.getFromCode3(countryCode3), City);
		}

		public String getCompanyName() {
			return CompanyName;
		}

		public void setCompanyName(String companyName) {
			CompanyName = companyName;
		}

		public String getCity() {
			return City;
		}

		public void setCity(String city) {
			City = city;
		}

		public String getCountryCode3() {
			return countryCode3;
		}

		public void setCountryCode3(String countryCode3) {
			this.countryCode3 = countryCode3;
		}

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	@SearcheableField(userFetchPath = "User.myWorkcursus.Work")
	private String CompanyName;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcountry")
	private Country Country;
	private String City;

	private Work(String companyName, models.Country country, String city) {
		CompanyName = companyName;
		Country = country;
		City = city;
	}

	public static Finder<Long, WorkCursus> find = new Finder<Long, WorkCursus>(WorkCursus.class);

	@Override
	public String toString() {
		return "Work [CompanyName=" + CompanyName + ", Country=" + Country + ", City=" + City + "]";
	}

	public String getCompanyName() {
		return CompanyName;
	}

	public void setCompanyName(String companyName) {
		CompanyName = companyName;
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