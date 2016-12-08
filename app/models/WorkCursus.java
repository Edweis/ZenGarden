package models;

import play.data.validation.Constraints;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

import models.tools.UserBelonging;

@Entity
public class WorkCursus extends Model implements UserBelonging {

	public static class Builder {

		public Date StartDate;
		private Integer DurationMonth;
		@Constraints.Required
		private String Position;
		private Boolean IsCurrentWork;

		public String validate() {
			return null;
		}

		public WorkCursus generate(User user, Work work) {
			return new WorkCursus(user, work, StartDate, DurationMonth, Position, IsCurrentWork);
		}

		public Integer getDurationMonth() {
			return DurationMonth;
		}

		public void setDurationMonth(Integer durationMonth) {
			DurationMonth = durationMonth;
		}

		public String getPosition() {
			return Position;
		}

		public void setPosition(String position) {
			Position = position;
		}

		public Boolean getIsCurrentWork() {
			return IsCurrentWork;
		}

		public void setIsCurrentWork(Boolean isCurrentWork) {
			IsCurrentWork = isCurrentWork;
		}

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iduser")
	private User User;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "idwork")
	private Work Work;
	private Date StartDate;
	private Integer DurationMonth;
	private String Position;
	private Boolean IsCurrentWork;

	private WorkCursus(models.User user, models.Work work, Date startDate, Integer durationMonth, String position,
			Boolean isCurrentWork) {
		User = user;
		Work = work;
		StartDate = startDate;
		DurationMonth = durationMonth;
		Position = position;
		IsCurrentWork = isCurrentWork;
	}

	public static Finder<Long, WorkCursus> find = new Finder<Long, WorkCursus>(WorkCursus.class);

	@Override
	public String toString() {
		return "WorkCursus [Id=" + Id + ", User=" + User + ", Work=" + Work + ", StartDate=" + StartDate + "]";
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

	public Work getWork() {
		return Work;
	}

	public void setWork(Work work) {
		Work = work;
	}

	public Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}

	public Integer getDurationMonth() {
		return DurationMonth;
	}

	public void setDurationMonth(Integer durationMonth) {
		DurationMonth = durationMonth;
	}

	public String getPosition() {
		return Position;
	}

	public void setPosition(String position) {
		Position = position;
	}

	public Boolean getIsCurrentWork() {
		return IsCurrentWork;
	}

	public void setIsCurrentWork(Boolean isCurrentWork) {
		IsCurrentWork = isCurrentWork;
	}

	@Override
	public boolean hasRight(models.User user) {
		return this.User.Id == user.Id;
	}

}