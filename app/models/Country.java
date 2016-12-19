package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Model;

import models.tools.SearcheableField;

@Entity
public class Country extends Model {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	@SearcheableField(userFetchPath = "User.myEducation.School.Country")
	// @SearcheableField(userFetchPath = "User.Nationality")
	private String Name;
	private String Code2;
	private String Code3;

	@Override
	public String toString() {
		return Name;
	}

	public static com.avaje.ebean.Model.Finder<Long, Country> find = new Finder<Long, Country>(Country.class);

	public static Country getFromCode3(String code3) {
		return Country.find.where().eq("Code3", code3).findUnique();
	}

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

	public String getCode2() {
		return Code2;
	}

	public void setCode2(String code2) {
		Code2 = code2;
	}

	public String getCode3() {
		return Code3;
	}

	public void setCode3(String code3) {
		Code3 = code3;
	}

}