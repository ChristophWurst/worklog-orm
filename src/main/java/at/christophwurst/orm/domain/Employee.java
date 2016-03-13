package at.christophwurst.orm.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
// Version 1: union-subclass in Hibernate
//@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)

// Version 2: subclass in Hibernate
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name="employee_type", 
//		discriminatorType=DiscriminatorType.STRING)
//@DiscriminatorValue("E")
// Version 3: joined-subclass in Hibernate
@Inheritance(strategy = InheritanceType.JOINED)
@NamedEntityGraph(name = "graph.Employee.logbookEntries",
	attributeNodes = @NamedAttributeNode(value = "projects", subgraph = "projGraph"),
	subgraphs = {
		@NamedSubgraph(name = "projGraph", attributeNodes = @NamedAttributeNode(value = "requirements", subgraph = "reqGraph")),
		@NamedSubgraph(name = "reqGraph", attributeNodes = {
		@NamedAttributeNode(value = "tasks", subgraph = "taskGraph"),
		@NamedAttributeNode(value = "sprint")
	}),
		@NamedSubgraph(name = "taskGraph", attributeNodes = @NamedAttributeNode(value = "logbookEntries"))
	})
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String firstName;
	private String lastName;

	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;

	// Version 1: serialized!
	// Version 2
	// @Embedded
	// @AttributeOverrides({
	// @AttributeOverride(name="zipCode", column = @Column(name =
	// "address_zipCode")),
	// @AttributeOverride(name="city", column = @Column(name = "address_city")),
	// @AttributeOverride(name="street", column = @Column(name =
	// "address_street"))
	// })
	// Version 3
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Address address;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<LogbookEntry> logbookEntries = new HashSet<>();

	@ManyToMany(mappedBy = "employees")
	private Set<Project> projects = new HashSet<>();

	public Employee() {
	}

	public Employee(String firstName, String lastName, Date dateOfBirth) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%d: %s, %s (%4$td.%4$tm.%4$tY)", id, lastName, firstName, dateOfBirth));
		if (address != null) {
			sb.append(", " + address);
		}
		return sb.toString();
	}

	public Set<LogbookEntry> getLogbookEntries() {
		return logbookEntries;
	}

	public void setLogbookEntries(Set<LogbookEntry> logbookEntries) {
		this.logbookEntries = logbookEntries;
	}

	public void addLogbookEntry(LogbookEntry entry) {
		if (entry == null) {
			throw new IllegalArgumentException("Logbook entry must not be null!");
		}

		if (entry.getEmployee() != null) {
			entry.getEmployee().getLogbookEntries().remove(entry);
		}

		this.getLogbookEntries().add(entry);
		entry.setEmployee(this);
	}

	public void removeLogbookEntry(LogbookEntry entry) {
		if (entry == null) {
			throw new IllegalArgumentException("Logbook entry must not be null!");
		}
		if (entry.getEmployee() != null && entry.getEmployee() != this) {
			throw new IllegalArgumentException("Can't remove logbook entry of another employee!");
		}

		this.getLogbookEntries().remove(entry);
		entry.setEmployee(null);
	}

	//
	// public void detach() {
	// for (LogbookEntry entry : new
	// ArrayList<LogbookEntry>(getLogbookEntries())) {
	// removeLogbookEntry(entry);
	// }
	// }
	//
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	public void addProject(Project project) {
		if (project == null) {
			throw new IllegalArgumentException("project must not be null!");
		}

		project.getEmployees().add(this);
		this.projects.add(project);
	}

}
