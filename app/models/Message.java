package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

	/**
	 * Regular message.
	 * 
	 * @author piou
	 *
	 */
	public static Message newRegularMessage(models.Chat chat, User writer, String content) {
		return new Message(chat, writer, content, "REGULAR");
	}

	/*
	 * Indicated that Writer has share his contact to With. We put the firstName
	 * of With in the content, front end will do the rest. TODO : Damn man
	 * that's ugly isn't it ?
	 */
	public static Message newShareContactMessage(models.Chat chat, User HasShared, User With) {
		return new Message(chat, HasShared, With.getFirstName(), "SHARECONTACT");
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	/**
	 * Is null if its a system message
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IdWriter")
	private User Writer;
	@NotNull(message = "Oups you forgot to write something")
	private String Content;
	@NotNull
	private Timestamp Date;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idchat")
	@NotNull
	private Chat Chat;
	@NotNull
	private String Type;
	@NotNull
	private Boolean Seen;

	@Override
	public String toString() {
		return "[@" + Date + "] [" + Writer + "]" + Content;
	}

	private Message(Chat chat, User writer, String content, String type) {
		this.Writer = writer;
		this.Content = content.replace("%%", "");
		this.Type = type;
		this.Date = new Timestamp(System.currentTimeMillis());
		this.Seen = false;
	}

	public static Finder<Long, Message> find = new Finder<Long, Message>(Message.class);

	public User getWriter() {
		return Writer;
	}

	public void setWriter(User writer) {
		Writer = writer;
	}

	public Long getId() {
		return Id;
	}

	public Chat getChat() {
		return Chat;
	}

	public void setChat(Chat chat) {
		Chat = chat;
	}

	public Boolean getSeen() {
		return Seen;
	}

	public void setSeen(Boolean seen) {
		Seen = seen;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public Timestamp getDate() {
		return Date;
	}

	public void setDate(Timestamp date) {
		Date = date;
	}

	/**
	 * Return all message from ts to now in the room
	 * 
	 * @param room
	 * @param ts
	 * @return
	 */
	public static List<Message> lastMessagesSince(Room room, Timestamp ts) {
		return Message.find.where().eq("Chat.Room", room).and().gt("Date", ts).findList();
	}

	/**
	 * return particular messages to be displayed in
	 * views.html.inc.comon.notification(). </br>
	 * They correspon to the last message sent but the content represents the
	 * number of messages unseen.
	 * 
	 * @param timestamp
	 * @return
	 */
	public static List<Message> unseenMessages(Timestamp timestamp, User cu) {
		List<Message> res = new ArrayList<Message>();

		// list of rooms where the user is
		List<Room> rooms = Room.find.select("Id").where().eq("Participants", cu).findList();

		// for each room, check the number of unseen message
		int count;
		Message m;
		for (Room r : rooms) {
			// number of messsage unseen by the cu in the room r
			count = Message.find.where().eq("Chat.Room.Id", r.getId()).eq("seen", false).ne("Writer", cu)
					.findRowCount();
			if (count > 0) {
				// last unseen message
				m = Message.find.setMaxRows(1).where().eq("Chat.Room.Id", r.getId()).eq("seen", false).ne("Writer", cu)
						.orderBy("Date desc").findUnique();
				m.setContent(count + "");
				res.add(m);
			}
		}

		return res;
	}

}