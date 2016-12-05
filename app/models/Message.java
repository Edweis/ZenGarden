package models;

import java.sql.Timestamp;

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
public class Message extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long Id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IdWriter")
	@NotNull
	public User Writer;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IdChat")
	@NotNull
	public Chat Chat;
	@NotNull(message = "Oups you forgot to write something")
	public String Content;
	@NotNull
	public Timestamp Date;

	@Override
	public String toString() {
		return Content;
	}

	public static Finder<Long, Message> find = new Finder<Long, Message>(Message.class);

}