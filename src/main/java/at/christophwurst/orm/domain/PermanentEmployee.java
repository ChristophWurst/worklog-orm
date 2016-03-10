package at.christophwurst.orm.domain;

import java.util.Date;

import javax.persistence.Entity;

@Entity

// Version 2: subclass in Hibernate
//@DiscriminatorValue("P")
public class PermanentEmployee extends Employee {

	private static final long serialVersionUID = 1L;

	private double salary;

	private double totalTime;

	public double getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(double totalTime) {
		this.totalTime = totalTime;
	}

	public PermanentEmployee() {
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public PermanentEmployee(String firstName, String lastName, Date dateOfBirth) {
		super(firstName, lastName, dateOfBirth);
	}

	@Override
	public String toString() {
		return super.toString() + ", salary = " + salary;
	}

}
