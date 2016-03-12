package at.christophwurst.orm.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Task implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	private String shortDescription;
	private String description;
	private int estimatedTime;
	
	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
	private Set<LogbookEntry> logbookEntries = new HashSet<>();
	
	@ManyToOne
	private Requirement requirement;

	public Task() {
	}

	public Task(String shortDescription) {
		this.shortDescription = shortDescription;
	}

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

	public int getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(int estCost) {
		this.estimatedTime = estCost;
	}

	public Set<LogbookEntry> getLogbookEntries() {
		return logbookEntries;
	}

	public void setLogbookEntries(Set<LogbookEntry> logbookEntries) {
		this.logbookEntries = logbookEntries;
	}
	
	public void addLogbookEntry(LogbookEntry entry) {
		if (entry.getTask() != null) {
			entry.getTask().getLogbookEntries().remove(entry);
		}
		logbookEntries.add(entry);
		entry.setTask(this);
	}

	public Requirement getRequirement() {
		return requirement;
	}

	public void setRequirement(Requirement requirement) {
		this.requirement = requirement;
	}

}
