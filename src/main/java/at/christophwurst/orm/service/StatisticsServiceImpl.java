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
import at.christophwurst.orm.domain.Project;
import at.christophwurst.orm.domain.Requirement;
import at.christophwurst.orm.domain.Sprint;
import at.christophwurst.orm.domain.Task;
import java.util.HashMap;
import java.util.Map;
import at.christophwurst.orm.dao.EmployeeRepository;
import at.christophwurst.orm.dao.ProjectRepository;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component()
class StatisticsServiceImpl implements StatisticsService {

	@Inject
	private ProjectRepository projectRepository;
	@Inject
	private EmployeeRepository employeeRepository;

	public void setProjectRepository(ProjectRepository projectDao) {
		this.projectRepository = projectDao;
	}

	public void setEmployeeRepository(EmployeeRepository employeeDao) {
		this.employeeRepository = employeeDao;
	}

	@Override
	public Map<Project, Long> getEmployeeTimeOnProject(Long employeeId) {
		Map<Project, Long> result = new HashMap<>();
		Employee empl = employeeRepository.getEmployeeAndLogbookEntries(employeeId);
		empl.getProjects().forEach((Project proj) -> {
			Long time = proj.getRequirements().stream().mapToLong((Requirement req) -> {
				return req.getTasks().stream().mapToLong((Task tsk) -> {
					return tsk.getLogbookEntries().stream().mapToLong(LogbookEntry::getTotalTime).sum();
				}).sum();
			}).sum();
			result.put(proj, time);
		});
		return result;
	}

	private Map<Sprint, Long> getEmployeeTimeOnSprints(Project proj) {
		Map<Sprint, Long> result = new HashMap<>();
		proj.getRequirements().stream().filter((Requirement req) -> {
			return req.getSprint() != null;
		}).forEach((Requirement req) -> {
			Sprint sprint = req.getSprint();
			Long time = req.getTasks().stream().mapToLong((Task tsk) -> {
				return tsk.getLogbookEntries().stream().mapToLong(LogbookEntry::getTotalTime).sum();
			}).sum();
			Long currValue = 0L;
			if (result.containsKey(sprint)) {
				currValue = result.remove(sprint);
			}
			currValue += time;
			result.put(sprint, currValue);
		});
		return result;
	}

	@Override
	public Map<Project, Map<Sprint, Long>> getEmployeeTimeOnSprint(Long employeeId) {
		Map<Project, Map<Sprint, Long>> result = new HashMap<>();
		Employee empl = employeeRepository.getEmployeeAndLogbookEntries(employeeId);
		empl.getProjects().forEach((Project proj) -> {
			result.put(proj, getEmployeeTimeOnSprints(proj));
		});
		return result;
	}

	@Override
	public Map<Employee, Long> getEmployeeTimeOnProjectPerEmployee(Long projectId) {
		Map<Employee, Long> result = new HashMap<>();
		Project p = projectRepository.findAndLoadLogbookEntries(projectId);
		p.getRequirements().forEach((Requirement req) -> {
			req.getTasks().forEach((Task tsk) -> {
				tsk.getLogbookEntries().forEach((LogbookEntry entry) -> {
					Employee empl = entry.getEmployee();
					Long currValue = 0L;
					if (result.containsKey(empl)) {
						currValue = result.remove(empl);
					}
					currValue += entry.getTotalTime();
					result.put(empl, currValue);
				});
			});
		});
		return result;
	}

	@Override
	public Map<Sprint, Long> getSprintTime(Long projectId) {
		Map<Sprint, Long> result = new HashMap<>();
		Project p = projectRepository.findAndLoadLogbookEntries(projectId);
		p.getRequirements().forEach((Requirement req) -> {
			if (req.getSprint() == null) {
				// Nothing to do
				return;
			}

			Sprint sprint = req.getSprint();
			Long time = req.getTasks().stream().mapToLong((Task tsk) -> {
				return tsk.getLogbookEntries().stream().mapToLong(LogbookEntry::getTotalTime).sum();
			}).sum();

			Long currValue = 0L;
			if (result.containsKey(sprint)) {
				currValue = result.remove(sprint);
			}
			currValue += time;
			result.put(sprint, currValue);
		});
		return result;
	}

	@Override
	public Map<Requirement, Long> getRequirementTime(Long projectId) {
		Map<Requirement, Long> result = new HashMap<>();
		Project p = projectRepository.findAndLoadLogbookEntries(projectId);
		p.getRequirements().forEach((Requirement req) -> {
			Long time = req.getTasks().stream().mapToLong((Task tsk) -> {
				return tsk.getLogbookEntries().stream().mapToLong(LogbookEntry::getTotalTime).sum();
			}).sum();
			result.put(req, time);
		});
		return result;
	}

}
