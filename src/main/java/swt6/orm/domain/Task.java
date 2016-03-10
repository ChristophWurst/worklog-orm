package swt6.orm.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Task implements Serializable {

	public Task() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	private String shortDescription;

	private String description;

	private int estCost;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShortDesc() {
		return shortDescription;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDescription = shortDesc;
	}

	public String getLongDesc() {
		return description;
	}

	public void setLongDesc(String longDesc) {
		this.description = longDesc;
	}

	public int getEstCost() {
		return estCost;
	}

	public void setEstCost(int estCost) {
		this.estCost = estCost;
	}

	@OneToMany(mappedBy = "task")
	private Set<LogbookEntry> logbookEntries = new HashSet<>();

	public Set<LogbookEntry> getLogbookEntries() {
		return logbookEntries;
	}

	public void setLogbookEntries(Set<LogbookEntry> logbookEntries) {
		this.logbookEntries = logbookEntries;
	}

	@ManyToOne
	private Requirement requirement;

	public Requirement getRequirement() {
		return requirement;
	}

	public void setRequirement(Requirement requirement) {
		this.requirement = requirement;
	}

}
