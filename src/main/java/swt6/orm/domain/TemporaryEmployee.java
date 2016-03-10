package swt6.orm.domain;

import java.text.DateFormat;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity

// Version 2: subclass in Hibernate
//@DiscriminatorValue("T")
public class TemporaryEmployee extends Employee {

	private static final long serialVersionUID = 1L;

	private static final DateFormat fmt = DateFormat.getDateInstance();

	private String renter;

	private double hourlyRate;

	private Date startDate;

	private Date endDate;

	public TemporaryEmployee() {
	}

	public TemporaryEmployee(String firstName, String lastName, Date dateOfBirth) {
		super(firstName, lastName, dateOfBirth);
	}

	public String getRenter() {
		return renter;
	}

	public void setRenter(String renter) {
		this.renter = renter;
	}

	public double getHourlyRate() {
		return hourlyRate;
	}

	public void setHourlyRate(double hourlyRate) {
		this.hourlyRate = hourlyRate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(", hourlyRate = " + hourlyRate);
		sb.append(", renter = " + renter);
		sb.append(", startDate = " + fmt.format(startDate));
		sb.append(", endDate = " + fmt.format(endDate));
		return sb.toString();
	}
}
