package at.christophwurst.orm.domain;

import com.sun.istack.internal.Nullable;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;

@Entity
@NamedEntityGraph(name = "graph.Project.logbookEntries",
	attributeNodes = {
		@NamedAttributeNode(value = "requirements", subgraph = "reqGraph"),
		@NamedAttributeNode(value = "sprints"),
		@NamedAttributeNode(value = "employees")
	},
	subgraphs = {
		@NamedSubgraph(name = "reqGraph", attributeNodes = {
		@NamedAttributeNode(value = "tasks", subgraph = "taskGraph"),
		@NamedAttributeNode(value = "sprint")
	}),
		@NamedSubgraph(name = "taskGraph", attributeNodes = @NamedAttributeNode(value = "logbookEntries", subgraph = "emplGraph")),
		@NamedSubgraph(name = "emplGraph", attributeNodes = @NamedAttributeNode(value = "employee")),})
public class Project implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	private String name;

	@ManyToMany(cascade = CascadeType.MERGE)
	private Set<Employee> employees = new HashSet<>();

	@OneToMany(mappedBy = "project", cascade = {CascadeType.MERGE})
	private Set<Sprint> sprints = new HashSet<>();

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	private Set<Requirement> requirements = new HashSet<>();

	@ManyToOne(cascade = CascadeType.MERGE)
	@Nullable
	private Employee owner;

	public Project() {
	}

	public Project(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	public void addMember(Employee employee) {
		if (employee == null) {
			throw new IllegalArgumentException("employee to add must not be null!");
		}
		employee.getProjects().add(this);
		employees.add(employee);
	}

	public void removeMember(Employee employee) {
		if (employee == null) {
			throw new IllegalArgumentException("employee to remove must not be null!");
		}
		employee.getProjects().remove(this);
		employees.remove(employee);
	}

	@Override
	public String toString() {
		return getName();
	}

	public Set<Sprint> getSprints() {
		return sprints;
	}

	public void setSprints(Set<Sprint> sprints) {
		this.sprints = sprints;
	}

	public void addSprint(Sprint sprint) {
		if (sprint.getProject() != null) {
			sprint.getProject().getSprints().remove(sprint);
		}
		sprints.add(sprint);
		sprint.setProject(this);
	}

	public Set<Requirement> getRequirements() {
		return requirements;
	}

	public void setRequirements(Set<Requirement> requirements) {
		this.requirements = requirements;
	}

	public void addRequirement(Requirement requirement) {
		if (requirement.getProject() != null) {
			requirement.getProject().getRequirements().remove(requirement);
		}
		requirements.add(requirement);
		requirement.setProject(this);
	}

	public Employee getOwner() {
		return owner;
	}

	public void setOwner(Employee owner) {
		if (owner == null) {
			if (this.owner != null) {
				this.owner.getOwnedProjects().remove(this);
			}
		} else {
			owner.getOwnedProjects().add(this);
		}
		this.owner = owner;
	}

}
