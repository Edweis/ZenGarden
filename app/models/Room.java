package models;

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
	public Long Id;
	@ManyToMany
	@JoinTable(name = "Chair", joinColumns = @JoinColumn(name = "IdRoom", referencedColumnName = "Id"), inverseJoinColumns = @JoinColumn(name = "IdUser", referencedColumnName = "Id"))
	public List<User> Participants;
	public Boolean Ongoing;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IdChat")
	public Chat Chat;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "IdAppointment")
	public Appointment Appointment;

	public String participantsToString() {
		String res = "";
		for (User u : Participants) {
			res = res + u.FirstName + ", ";
		}
		res = res.substring(0, res.length() - 2);
		return res;
	}

	public static Finder<Long, Room> find = new Finder<Long, Room>(Room.class);
}