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
	private Long Id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IdWriter")
	@NotNull
	private User Writer;
	@NotNull(message = "Oups you forgot to write something")
	private String Content;
	@NotNull
	private Timestamp Date;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idchat")
	@NotNull
	private Chat Chat;

	@Override
	public String toString() {
		return "[" + Date + "] " + Content;
	}

	public Message(Chat chat, User writer, String content) {
		this.Writer = writer;
		this.Content = content;
		this.Date = new Timestamp(System.currentTimeMillis());
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

}