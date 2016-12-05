package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.avaje.ebean.Model;

import models.annotations.Searchable;

@Entity
public class School extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long Id;
	@Searchable(userFetchPath = "User.myEducation.School")
	public String Name;
	@NotNull(message = "Please insert a country")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "IdCountry")
	public Country Country;
	public String City;

	@Override
	public String toString() {
		return Name + " (" + Country.Code3 + ")";
	}

	public static Finder<Long, School> find = new Finder<Long, School>(School.class);
}