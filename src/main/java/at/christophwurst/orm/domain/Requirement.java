package at.christophwurst.orm.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Requirement implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String shortDesc;
	private String longDesc;

	@ManyToOne(cascade = CascadeType.ALL)
	private Project project;

	@ManyToOne(optional = true, cascade = CascadeType.ALL)
	private Sprint sprint;

	@OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Task> tasks = new HashSet<>();

	public Requirement() {
	}

	public Requirement(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String getLongDesc() {
		return longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	public Sprint getSprint() {
		return sprint;
	}

	public void setSprint(Sprint sprint) {
		this.sprint = sprint;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	public void addTask(Task task) {
		if (task.getRequirement() != null) {
			task.getRequirement().getTasks().remove(task);
		}
		tasks.add(task);
		task.setRequirement(this);
	}

	public void removeTask(Task task) {
		if (task != null) {
			task.setRequirement(null);
		}
		tasks.remove(task);
	}

	@Override
	public String toString() {
		return shortDesc;
	}

}
