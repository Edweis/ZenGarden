package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.avaje.ebean.Model;

@Entity
public class Chat extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long Id;
	@NotNull(message = "Please insert a title to initiate the chat")
	@Column(name = "Title")
	public String TitleChat;
	@OneToMany(mappedBy = "Chat")
	public List<Message> Messages;
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "Chat")
	public Room Room;

	public Message getLatestMessage() {
		if (Messages == null) {
			return null;
		}
		if (Messages.isEmpty()) {
			return null;
		}
		Message latest = Messages.get(0);
		for (Message m : Messages) {

			if (m.Date.getTime() < latest.Date.getTime()) {
				latest = m;
			}
		}
		return latest;
	}

	public static Finder<Long, Chat> find = new Finder<Long, Chat>(Chat.class);

}