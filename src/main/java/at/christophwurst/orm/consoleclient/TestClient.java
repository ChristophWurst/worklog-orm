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
import at.christophwurst.orm.service.BurnDownService;
import at.christophwurst.orm.service.ProjectService;
import at.christophwurst.orm.service.ScrumService;
import at.christophwurst.orm.service.ServiceContainer;
import at.christophwurst.orm.service.StatisticsService;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class TestClient {

	public static void main(String[] args) {
		new TestClient().run();
	}

	private final StatisticsService statisticsService;
	private final ScrumService scrumService;
	private final BurnDownService burnDownService;
	private final ProjectService projectService;

	public TestClient() {
		statisticsService = ServiceContainer.getStatisticsService();
		scrumService = ServiceContainer.getScrumService();
		burnDownService = ServiceContainer.getBurnDownService();
		projectService = ServiceContainer.getProjectService();
	}

	private long msToHours(long val) {
		return val / (1000 * 3600);
	}

	private void showEmployeeTimePerProject() {
		statisticsService.getEmployeeTimeOnProjectPerEmployee().forEach((Project p, Map<Employee, Long> stat) -> {
			System.out.println("# Project " + p);
			stat.forEach((Employee e, Long time) -> {
				System.out.println("  - " + e + ": " + msToHours(time) + "h");
			});
		});
	}

	private void showSprintTimePerProject() {
		statisticsService.getSprintTimePerProject().forEach((Project p, Map<Sprint, Long> stat) -> {
			System.out.println("# Project " + p);
			stat.forEach((Sprint sprint, Long time) -> {
				System.out.println("  - " + sprint + ": " + msToHours(time) + "h");
			});
		});
	}

	private void showRequiremntTimePerProject() {
		statisticsService.getRequirementTimePerProject().forEach((Project p, Map<Requirement, Long> stat) -> {
			System.out.println("# Project " + p);
			stat.forEach((Requirement r, Long time) -> {
				System.out.println("  - " + r + ": " + msToHours(time) + "h");
			});
		});
	}

	private void showProjectTimePerEmployee() {
		statisticsService.getEmployeeTimeOnProject().forEach((Employee empl, Map<Project, Long> stat) -> {
			System.out.println("# Employee " + empl);
			stat.forEach((Project proj, Long time) -> {
				System.out.println("  - " + proj + ": " + msToHours(time) + "h");
			});
		});
	}

	private void showProjectAndSprintTimePerEmployee() {
		statisticsService.getEmployeeTimeOnSprint().forEach((Employee empl, Map<Project, Map<Sprint, Long>> stat) -> {
			System.out.println("# Employee " + empl);
			stat.forEach((Project proj, Map<Sprint, Long> projStat) -> {
				System.out.println("  - Project " + proj);
				projStat.forEach((Sprint sprint, Long time) -> {
					System.out.println("    - Sprint " + sprint + ": " + msToHours(time) + "h");
				});
			});
		});
	}

	private void showBurnDownCharts() {
		System.out.println("# BurnDown charts:");
		scrumService.getAllSprints().forEach((Sprint sprint) -> {
			System.out.println("  - Sprint " + sprint);
			burnDownService.getBurnDownData(sprint).forEach((Date d, Float val) -> {
				System.out.println("    - " + d + ": " + val);
			});
		});
	}

	private void showProjectCosts() {
		System.out.println("# Project costs:");
		projectService.getProjectCosts().forEach((p, c) -> {
			System.out.println(p + ": " + c);
		});
	}

	public void run() {
		System.out.print("Scrum project test client started");

		new DbSeeder().seed();

		showEmployeeTimePerProject();
		showSprintTimePerProject();
		showRequiremntTimePerProject();
		showProjectTimePerEmployee();
		showProjectAndSprintTimePerEmployee();

		showBurnDownCharts();
		showProjectCosts();
	}

}
