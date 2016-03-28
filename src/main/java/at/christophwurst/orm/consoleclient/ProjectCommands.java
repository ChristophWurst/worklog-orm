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
package at.christophwurst.orm.consoleclient;

import at.christophwurst.orm.domain.Employee;
import at.christophwurst.orm.domain.Project;
import at.christophwurst.orm.domain.Requirement;
import at.christophwurst.orm.domain.Sprint;
import at.christophwurst.orm.domain.Task;
import at.christophwurst.orm.service.EmployeeService;
import at.christophwurst.orm.service.ProjectService;
import java.util.Set;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
@Component
public class ProjectCommands {

	@Inject
	private ProjectService projectService;
	@Inject
	private EmployeeService employeeService;

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public void registerCommands(Client client) {
		client.registerCommand("project:list", (consoleInterface) -> {
			System.out.println("# Projects");
			projectService.getAllProjects().forEach(p -> {
				System.out.println(" - " + p.getId() + ": " + p);
				System.out.println("   - Owner: " + p.getOwner());
			});
		});

		client.registerCommand("project:show", (consoleInterface) -> {
			Long id = consoleInterface.getLongValue("project id");
			Project pro = projectService.getProjectById(id);
			System.out.println(pro);
		});

		client.registerCommand("project:create", (consoleInterface) -> {
			String name = consoleInterface.getStringValue("name");

			Project project = new Project(name);

			projectService.saveProject(project);
		});

		client.registerCommand("project:owner", (consoleInterface) -> {
			Long projectId = consoleInterface.getLongValue("project id");
			Long employeeId = consoleInterface.getLongValue("employee id");

			Project project = projectService.getProjectById(projectId);
			Employee employee = null;
			if (employeeId != null) {
				employee = employeeService.getById(employeeId);
			}

			project.setOwner(employee);
			projectService.saveProject(project);
		});

		client.registerCommand("project:employees", (consoleInterface) -> {
			Long id = consoleInterface.getLongValue("project id");
			projectService.getProjectById(id).getEmployees().forEach(e -> {
				System.out.println(" - " + e.getId() + ": " + e);
			});
		});

		client.registerCommand("project:employee:add", (consoleInterface) -> {
			Long projectId = consoleInterface.getLongValue("project id");
			Long employeeId = consoleInterface.getLongValue("employee id");

			Project project = projectService.getProjectById(projectId);
			Employee employee = employeeService.getById(employeeId);

			project.addMember(employee);
			projectService.saveProject(project);
		});

		client.registerCommand("project:employee:remove", (consoleInterface) -> {
			Long projectId = consoleInterface.getLongValue("project id");
			Long employeeId = consoleInterface.getLongValue("employee id");

			Project project = projectService.getProjectById(projectId);
			Employee employee = employeeService.getById(employeeId);

			project.removeMember(employee);
			projectService.saveProject(project);
		});

		client.registerCommand("project:requirements", (consoleInterface) -> {
			Long id = consoleInterface.getLongValue("project id");
			Project project = projectService.getProjectById(id);

			project.getRequirements().forEach(r -> {
				System.out.println(" - " + r.getId() + ": " + r);
			});
		});

		client.registerCommand("project:requirement:add", (consoleInterface) -> {
			Long id = consoleInterface.getLongValue("project id");
			String shortDesc = consoleInterface.getStringValue("short description");

			Project project = projectService.getProjectById(id);
			Requirement requirement = new Requirement(shortDesc);

			project.addRequirement(requirement);
			projectService.saveProject(project);
		});

		client.registerCommand("project:requirements", (consoleInterface) -> {
			Long id = consoleInterface.getLongValue("project id");
			Project project = projectService.getProjectById(id);

			project.getRequirements().forEach(r -> {
				System.out.println(" - " + r.getId() + ": " + r);
			});
		});

		client.registerCommand("project:requirement:tasks", (consoleInterface) -> {
			Long id = consoleInterface.getLongValue("requirement id");
			Requirement requirement = projectService.getRequirementById(id);

			requirement.getTasks().forEach(t -> {
				System.out.println(" - " + t.getId() + ": " + t);
			});
		});

		client.registerCommand("project:requirement:task:add", (consoleInterface) -> {
			Long id = consoleInterface.getLongValue("requirement id");
			String shortDescription = consoleInterface.getStringValue("short description");

			Requirement requirement = projectService.getRequirementById(id);
			Task task = new Task(shortDescription);

			requirement.addTask(task);
			projectService.saveRequirement(requirement);
		});

		client.registerCommand("project:requirement:task:delete", (consoleInterface) -> {
			Long requirementId = consoleInterface.getLongValue("requirement id");
			Long taskId = consoleInterface.getLongValue("task id");

			Requirement requirement = projectService.getRequirementById(requirementId);
			Task task = projectService.getTaskById(taskId);

			requirement.removeTask(task);
			projectService.saveRequirement(requirement);
		});

		client.registerCommand("project:costs", (consoleInterface) -> {
			Long id = consoleInterface.getLongValue("project id");
			System.out.println(projectService.getProjectCosts(id));
		});

		client.registerCommand("project:sprints", (consoleInterface) -> {
			Long id = consoleInterface.getLongValue("project id");
			Project p = projectService.getProjectById(id);
			Set<Sprint> sprints = p.getSprints();
			if (sprints == null) {
				System.out.println("no sprints");
			} else {
				sprints.stream().forEach((s) -> {
					System.out.println(" - " + s);
				});
			}
		});
	}

}
