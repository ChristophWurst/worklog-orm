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

import at.christophwurst.orm.domain.Project;
import at.christophwurst.orm.domain.Sprint;
import at.christophwurst.orm.service.ProjectService;
import java.util.Set;
import javax.inject.Inject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
@Component
public class ProjectCommands {

	@Inject
	private ProjectService projectService;

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public void registerCommands(CommandDispatcher dispatcher) {
		dispatcher.registerCommand("project:list", (consoleInterface) -> {
			System.out.println("# Projects");
			projectService.getAllProjects().forEach(p -> {
				System.out.println(" - " + p);
			});
		});

		dispatcher.registerCommand("project:show", (consoleInterface) -> {
			Long id = consoleInterface.getValue("id");
			Project pro = projectService.getById(id);
			System.out.println(pro);
		});

		dispatcher.registerCommand("project:costs", (consoleInterface) -> {
			System.out.println("# Project costs:");
			projectService.getProjectCosts().forEach((p, c) -> {
				System.out.println(p + ": " + c);
			});
		});

		dispatcher.registerCommand("project:sprints", (consoleInterface) -> {
			Long id = consoleInterface.getValue("Project ID");
			Project p = projectService.getById(id);
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
