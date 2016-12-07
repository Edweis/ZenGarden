package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Model;

import models.tools.Searchable;

@Entity
public class Country extends Model {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long Id;
	@Searchable(userFetchPath = "User.myEducation.School.Country")
	public String Name;
	public String Code2;
	public String Code3;

	@Override
	public String toString() {
		return Name;
	}

	public static Finder<Long, Country> find = new Finder<Long, Country>(Country.class);

	public static Country getFromCode3(String code3) {
		return Country.find.where().eq("Code3", code3).findUnique();
	}

}