package models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

@Entity
public class Relationship extends Model {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idHasShared")
	private User HasShared;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idWith")
	private User With;
	private Timestamp Date;

	public static Finder<Long, Relationship> find = new Finder<Long, Relationship>(Relationship.class);

	/**
	 * Share contact information of HasShare to With
	 * 
	 * @param HasShared
	 * @param With
	 */
	public Relationship(User HasShared, User With) {
		this.HasShared = HasShared;
		this.With = With;
		Date = new Timestamp(System.currentTimeMillis());
	}

	public static Relationship hasHeSharedWith(User connectedUser, User interlocutor) {
		return Relationship.find.where().eq("HasShared", connectedUser).and().eq("With", interlocutor).findUnique();
	}

	public User getHasShared() {
		return HasShared;
	}

	public void setHasShared(User hasShared) {
		HasShared = hasShared;
	}

	public User getWith() {
		return With;
	}

	public void setWith(User with) {
		With = with;
	}

	public Timestamp getDate() {
		return Date;
	}

	public void setDate(Timestamp date) {
		Date = date;
	}

}
