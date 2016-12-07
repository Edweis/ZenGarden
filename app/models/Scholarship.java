package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.avaje.ebean.Model;

import models.tools.Searchable;

@Entity
public class Scholarship extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long Id;
	@Searchable(userFetchPath = "User.myFunding.Scholarship")
	@NotNull(message = "Please insert a title")
	public String Title;
	@NotNull(message = "Please insert a year")
	public String Year;
	public String Ref;

	@Override
	public String toString() {
		return Title;
	}

	public static Finder<Long, Scholarship> find = new Finder<Long, Scholarship>(Scholarship.class);
}
