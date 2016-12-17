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

import controllers.tools.AskForNewRequestResultException;

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
	 * Share contact information of HasShare to With. Also send a system message
	 * to a room with only these two people (that should exists if on have been
	 * on the chat but I'll check anyway).
	 * 
	 * @param HasShared
	 * @param With
	 * @throws AskForNewRequestResultException
	 */
	public Relationship(User HasShared, User With) throws AskForNewRequestResultException {
		this.HasShared = HasShared;
		this.With = With;
		this.Date = new Timestamp(System.currentTimeMillis());

		// Send a message to a chat room with ONLY HasShared and With
		Room r = Room.getRoomBetween(HasShared, With);
		if (r == null || r.getParticipants().size() != 2) {
			r = Room.createDefaultChatRoom(HasShared, With);
		}

		// send the message
		Message message = Message.newShareContactMessage(r.getChat(), HasShared, With);
		r.getChat().send(message);
	}

	public static Relationship hasHeSharedWith(User hasshared, User with) {
		return Relationship.find.where().eq("HasShared", hasshared).and().eq("With", with).findUnique();
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
