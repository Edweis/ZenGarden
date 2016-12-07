package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.avaje.ebean.Model;

import models.tools.SearcheableField;

@Entity
public class Scholarship extends Model {

	public static class Builder {

		@Constraints.Required
		public String scholarshipTitle;
		@Constraints.Required
		public String scholarshipYear;
		public String scholarshipRef;

		public String validate() {
			return null;
		}

		public Scholarship generate() {
			return new Scholarship(scholarshipTitle, scholarshipYear, scholarshipRef);
		}

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	@SearcheableField(userFetchPath = "User.myFunding.Scholarship")
	@NotNull(message = "Please insert a title")
	private String Title;
	@NotNull(message = "Please insert a year")
	private String Year;
	private String Ref;

	@Override
	public String toString() {
		return Title;
	}

	protected Scholarship(String title, String year, String ref) {
		Title = title;
		Year = year;
		Ref = ref;
	}

	public static Finder<Long, Scholarship> find = new Finder<Long, Scholarship>(Scholarship.class);

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getYear() {
		return Year;
	}

	public void setYear(String year) {
		Year = year;
	}

	public String getRef() {
		return Ref;
	}

	public void setRef(String ref) {
		Ref = ref;
	}
}
