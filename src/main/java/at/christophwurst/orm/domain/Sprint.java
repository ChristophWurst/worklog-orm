package at.christophwurst.orm.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

@Entity
@NamedEntityGraph(name = "graph.Sprint.logbookEntries",
	attributeNodes = @NamedAttributeNode(value = "requirements", subgraph = "reqGraph"),
	subgraphs = {
		@NamedSubgraph(name = "reqGraph", attributeNodes = @NamedAttributeNode(value = "tasks", subgraph = "taskGraph")),
		@NamedSubgraph(name = "taskGraph", attributeNodes = @NamedAttributeNode(value = "logbookEntries"))
	})
public class Sprint implements Serializable {

	public Sprint() {
	}

	public Sprint(int nr) {
		this.nr = nr;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private int nr;
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date startDate;
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date endDate;
	@ManyToOne
	private Project project;
	@OneToMany
	private Set<Requirement> requirements = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNr() {
		return nr;
	}

	public void setNr(int nr) {
		this.nr = nr;
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Set<Requirement> getRequirements() {
		return requirements;
	}

	public void setRequirements(Set<Requirement> requirements) {
		this.requirements = requirements;
	}

	public void addRequirement(Requirement requirement) {
		if (requirement.getSprint() != null) {
			requirement.getSprint().getRequirements().remove(requirement);
		}
		requirements.add(requirement);
		requirement.setSprint(this);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Sprint) {
			return Objects.equals(((Sprint) o).getId(), this.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + Objects.hashCode(this.id);
		hash = 97 * hash + this.nr;
		hash = 97 * hash + Objects.hashCode(this.startDate);
		hash = 97 * hash + Objects.hashCode(this.endDate);
		return hash;
	}

}
