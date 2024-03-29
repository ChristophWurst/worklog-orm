/*
 * Copyright (C) 2016 Christoph Wurst <christoph@winzerhof-wurst.at>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.christophwurst.orm.service;

import at.christophwurst.orm.domain.Employee;
import at.christophwurst.orm.domain.LogbookEntry;
import at.christophwurst.orm.domain.PermanentEmployee;
import at.christophwurst.orm.domain.Project;
import at.christophwurst.orm.domain.TemporaryEmployee;
import java.util.List;
import at.christophwurst.orm.dao.ProjectRepository;
import at.christophwurst.orm.dao.RequirementRepository;
import at.christophwurst.orm.dao.TaskRepository;
import at.christophwurst.orm.domain.Requirement;
import at.christophwurst.orm.domain.Task;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
class ProjectServiceImpl implements ProjectService {

	@Inject
	private ProjectRepository projectRepository;
	@Inject
	private RequirementRepository requirementRepository;
	@Inject
	private TaskRepository taskRepository;

	public void setProjectRepository(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	public void setRequirementRepository(RequirementRepository requirementRepository) {
		this.requirementRepository = requirementRepository;
	}

	public void setTaskRepository(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Override
	public double getProjectCosts(Long id) {
		Project p = projectRepository.findAndLoadLogbookEntries(id);
		double costs = p.getEmployees().stream().mapToDouble((Employee empl) -> {
			if (empl instanceof TemporaryEmployee) {
				TemporaryEmployee tmp = (TemporaryEmployee) empl;
				long time = p.getRequirements().stream().mapToLong((r) -> {
					return r.getTasks().stream().mapToLong((t) -> {
						return t.getLogbookEntries().stream().filter((l) -> {
							return l.getEmployee().equals(empl);
						}).mapToLong(LogbookEntry::getTotalTime).sum();
					}).sum();
				}).sum();
				return (float) (time / (1000 * 3600) * tmp.getHourlyRate());
			}
			if (empl instanceof PermanentEmployee) {
				PermanentEmployee perm = (PermanentEmployee) empl;
				return (float) (perm.getSalary() / (1000 * 3600));
			}
			return 0f;
		}).sum();
		return costs;

	}

	@Override
	public List<Project> getAllProjects() {
		return projectRepository.findAll();
	}

	@Override
	public Project getProjectById(Long id) {
		return projectRepository.findOne(id);
	}

	@Override
	public Project saveProject(Project project) {
		return projectRepository.save(project);
	}

	@Override
	public Requirement getRequirementById(Long id) {
		return requirementRepository.findOne(id);
	}

	@Override
	public Requirement saveRequirement(Requirement requirement) {
		return requirementRepository.save(requirement);
	}

	@Override
	public Task getTaskById(Long taskId) {
		return taskRepository.findOne(taskId);
	}

}
