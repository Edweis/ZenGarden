package models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import models.annotations.Searchable;

@Entity
public class Work extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long Id;
	@Searchable(userFetchPath = "User.myWorkcursus.Work")
	public String CompanyName;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Idcountry")
	public Country Country;
	public String City;

	public static Finder<Long, WorkCursus> find = new Finder<Long, WorkCursus>(WorkCursus.class);
}