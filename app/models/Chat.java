package models;

import play.mvc.Results;

import java.util.List;

import javax.persistence.CascadeType;
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

import controllers.tools.AskForNewRequestResultException;

@Entity
public class Chat extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	@NotNull(message = "Please insert a title to initiate the chat")
	@Column(name = "Title")
	private String TitleChat;
	@OneToMany(mappedBy = "Chat", cascade = CascadeType.PERSIST)
	private List<Message> Messages;
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "Chat")
	private Room Room;

	public static Finder<Long, Chat> find = new Finder<Long, Chat>(Chat.class);

	public Chat(String TitleChat, Room room) {
		this.TitleChat = TitleChat;
		this.Room = room;
	}

	@Override
	public String toString() {
		return "Chat [Id=" + Id + ", TitleChat=" + TitleChat + ", Room=" + Room + "]";
	}

	/**
	 * Send a message in the chat.
	 * 
	 * @param message
	 * @throws AskForNewRequestResultException
	 *             if the writer of the message doesn't have the right to write
	 *             in this room.
	 */
	public void send(Message message) throws AskForNewRequestResultException {
		if (!this.getRoom().getParticipants().contains(message.getWriter())) {
			throw new AskForNewRequestResultException(Results.unauthorized("You dont belong to this room"));
		}
		this.Messages.add(message);
		this.update();
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getTitleChat() {
		return TitleChat;
	}

	public void setTitleChat(String titleChat) {
		TitleChat = titleChat;
	}

	public List<Message> getMessages() {
		return Messages;
	}

	public void setMessages(List<Message> messages) {
		Messages = messages;
	}

	public Room getRoom() {
		return Room;
	}

	public void setRoom(Room room) {
		Room = room;
	}

}