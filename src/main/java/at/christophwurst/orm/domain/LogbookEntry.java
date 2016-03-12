package at.christophwurst.orm.domain;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class LogbookEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String activity;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, optional = false)
	private Employee employee;

	@ManyToOne
	private Task task;

	public LogbookEntry() {
	}

	public LogbookEntry(String activity, Date startTime, Date endTime) {
		super();
		this.activity = activity;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public long getTotalTime() {
		return endTime.getTime() - startTime.getTime();
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public void attachEmployee(Employee employee) {
		if (employee == null) {
			throw new IllegalArgumentException("employee must not be null!");
		}

		if (this.employee != null) {
			this.employee.getLogbookEntries().remove(this);
		}

		this.employee = employee;
		this.employee.getLogbookEntries().add(this);
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@Override
	public String toString() {
		DateFormat fmt = DateFormat.getDateTimeInstance();
		return activity + ": " + fmt.format(startTime) + " - " + fmt.format(endTime) + "(" + (getEmployee() != null ? getEmployee().getLastName() : "NONE") + ")";
	}

}
