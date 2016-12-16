package models;

import play.data.validation.Constraints;

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
public class Contact extends Model {

	public static class Builder {
		@Constraints.Required()
		private String Type;
		@Constraints.Required()
		private String Value;

		public String validate() {
			return null;
		}

		public Contact generate(User user) {
			return new Contact(user, Type, Value);
		}

		public String getType() {
			return Type;
		}

		public void setType(String type) {
			Type = type;
		}

		public String getValue() {
			return Value;
		}

		public void setValue(String value) {
			Value = value;
		}

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IdUser")
	@NotNull(message = "the contact should have a user")
	private User User;
	@NotNull(message = "the contact should have a type")
	private String Type;
	@NotNull(message = "the contact should have a value")
	private String Value;

	private Contact(models.User user, String type, String value) {
		User = user;
		Type = type;
		Value = value;
	}

	@Override
	public String toString() {
		return "Contact [Id=" + Id + ", User=" + User + ", Type=" + Type + ", Value=" + Value + "]";
	}

	public static Finder<Long, Contact> find = new Finder<Long, Contact>(Contact.class);

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public User getUser() {
		return User;
	}

	public void setUser(User user) {
		User = user;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

}