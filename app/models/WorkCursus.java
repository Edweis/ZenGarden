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
public class WorkCursus extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long Id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IdUser")
	public User User;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IdWork")
	public Work Work;
	public Timestamp StartDate;
	public Integer DurationMonth;
	public String Position;
	public Boolean IsCurrentWork;

	public static Finder<Long, WorkCursus> find = new Finder<Long, WorkCursus>(WorkCursus.class);
}