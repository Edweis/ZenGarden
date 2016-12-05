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
import javax.validation.constraints.Pattern;

import com.avaje.ebean.Model;

import models.annotations.Searchable;

@Entity
public class Education extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long Id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IdUser")
	@NotNull
	public User User;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "IdSchool")
	public School School;
	public Integer DurationMonth;
	public Integer StartYear;
	public String Promotion;
	@Searchable(userFetchPath = "User.myEducation")
	public String Major;
	public Boolean IsHomeUniversity;
	public Boolean IsCurrentEducation;
	@Pattern(regexp = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)", message = "Please select a valide mail address")
	public String VerifEmail;
	public Boolean IsEmailVerified;

	public static Finder<Long, Education> find = new Finder<Long, Education>(Education.class);
}