package models;

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
public class Contact extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long Id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IdUser")
	@NotNull(message = "the contact should have a user")
	public User User;
	@NotNull(message = "the contact should have a type")
	public String Type;
	@NotNull(message = "the contact should have a value")
	public String Value;

	@Override
	public String toString() {
		return "for:" + User + ", " + Type + ", " + Value;
	}

	public static Finder<Long, Contact> find = new Finder<Long, Contact>(Contact.class);
}