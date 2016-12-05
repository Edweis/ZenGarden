package models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

import com.avaje.ebean.Model;

@Entity
public class Appointment extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long Id;
	@PrimaryKeyJoinColumn
	public Room Room;
	@NotNull(message = "Please insert a title to begin the chat")
	@Column(name = "Title")
	public String TitleAppointment;
	public Timestamp Date;
	public String Place;
	public Integer Price;

	public String Comment;
	public Integer Rating;

	public static Finder<Long, Appointment> find = new Finder<Long, Appointment>(Appointment.class);

}