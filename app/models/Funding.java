package models;

import play.data.validation.Constraints;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.avaje.ebean.Model;

import models.tools.UserBelonging;

@Entity
public class Funding extends Model implements UserBelonging {

	public static class Builder {

		@Constraints.Required
		private String Amount;

		public String getAmount() {
			return Amount;
		}

		public void setAmount(String amount) {
			Amount = amount;
		}

		public String validate() {
			return null;
		}

		public Funding generate(User user, Scholarship scholarship) {
			return new Funding(user, scholarship, Amount);
		}

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IdUser")
	@NotNull
	private User User;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "IdScholarship")
	@NotNull
	private Scholarship Scholarship;
	@NotNull(message = "Please insert an amount")
	private String Amount;

	public static Finder<Long, Funding> find = new Finder<Long, Funding>(Funding.class);

	private Funding(models.User user, models.Scholarship scholarship, String amount) {
		User = user;
		Scholarship = scholarship;
		Amount = amount;
	}

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

	public Scholarship getScholarship() {
		return Scholarship;
	}

	public void setScholarship(Scholarship scholarship) {
		Scholarship = scholarship;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	@Override
	public boolean hasRight(models.User user) {
		return this.User.Id == user.Id;
	}

}