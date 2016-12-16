package models;

import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.avaje.ebean.Model;

@Entity
public class Room extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(name = "chair", joinColumns = @JoinColumn(name = "idroom", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "iduser", referencedColumnName = "id"))
	private List<User> Participants;
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "idchat")
	private Chat Chat;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "idappointment")
	private Appointment Appointment;

	public Room(User a, User b) {
		Participants = Arrays.asList(a, b);
	}

	@Override
	public String toString() {
		return "Room [Id=" + Id + ", Participants=" + Participants + "]";
	}

	public static Finder<Long, Room> find = new Finder<Long, Room>(Room.class);

	/**
	 * Return the (only) private room between two users. Return null if it
	 * doesen't exists yet. TODO: is their an Ebean method to look for several
	 * elements in a ManyToMany relationship ?
	 * 
	 * @param listUser
	 * @return
	 */
	public static Room getRoomBetween(User a, User b) {
		List<Room> rA = Room.find.where().eq("Participants", a).findList();
		for (Room r : rA) {
			if (r.getParticipants().contains(b)) {
				return r;
			}
		}
		return null;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public List<User> getParticipants() {
		return Participants;
	}

	public void setParticipants(List<User> participants) {
		Participants = participants;
	}

	public Chat getChat() {
		return Chat;
	}

	public void setChat(Chat chat) {
		Chat = chat;
	}

	public Appointment getAppointment() {
		return Appointment;
	}

	public void setAppointment(Appointment appointment) {
		Appointment = appointment;
	}

}