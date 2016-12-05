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

@Entity
public class Funding extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long Id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IdUser")
	@NotNull
	public User User;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "IdScholarship")
	@NotNull
	public Scholarship Scholarship;
	@NotNull(message = "Please insert an amount")
	public String Amount;

	public static Finder<Long, Funding> find = new Finder<Long, Funding>(Funding.class);

}