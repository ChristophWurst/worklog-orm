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
import at.christophwurst.orm.service.StatisticsService;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
@Component
public class StatisticsCommands {

	@Inject
	private StatisticsService statisticsService;

	public void setStatisticsService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	public void registerCommands(Client client) {
		client.registerCommand("statistics:project:employee", (consoleInterface) -> {
			Long id = consoleInterface.getLongValue("project id");
			statisticsService.getEmployeeTimeOnProjectPerEmployee(id).forEach((Employee e, Long time) -> {
				time = time / (1000 * 3600);
				System.out.println("  - " + e + ": " + time + "h");
			});
		});

		client.registerCommand("statistics:project:sprint", (consoleInterface) -> {
			Long id = consoleInterface.getLongValue("project id");
			statisticsService.getSprintTime(id).forEach((Sprint sprint, Long time) -> {
				time = time / (1000 * 3600);
				System.out.println("  - " + sprint + ": " + time + "h");
			});
		});

		client.registerCommand("statistics:project:requirements", (consoleInterface) -> {
			Long id = consoleInterface.getLongValue("project id");
			statisticsService.getRequirementTime(id).forEach((Requirement r, Long time) -> {
				time = time / (1000 * 3600);
				System.out.println("  - " + r + ": " + time + "h");
			});
		});

		client.registerCommand("statistics:employee:project", (consoleInterface) -> {
			Long id = consoleInterface.getLongValue("employee id");
			statisticsService.getEmployeeTimeOnProject(id).forEach((Project proj, Long time) -> {
				time = time / (1000 * 3600);
				System.out.println("  - " + proj + ": " + time + "h");
			});
		});

		client.registerCommand("statistics:employee:projectsprint", (consoleInterface) -> {
			Long id = consoleInterface.getLongValue("employee id");
			statisticsService.getEmployeeTimeOnSprint(id).forEach((Project proj, Map<Sprint, Long> projStat) -> {
				System.out.println("  - Project " + proj);
				projStat.forEach((Sprint sprint, Long time) -> {
					time = time / (1000 * 3600);
					System.out.println("    - Sprint " + sprint + ": " + time + "h");
				});
			});
		});

	}

}
