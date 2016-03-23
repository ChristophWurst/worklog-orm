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

import at.christophwurst.orm.dao.EmployeeRepository;
import at.christophwurst.orm.dao.ProjectRepository;
import at.christophwurst.orm.domain.Employee;
import at.christophwurst.orm.domain.Project;
import at.christophwurst.orm.domain.Requirement;
import at.christophwurst.orm.domain.Sprint;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class StatisticsServiceImplTest {

	private ProjectRepository projectRepository;
	private EmployeeRepository employeeRepository;
	private StatisticsServiceImpl service;

	@Before
	public void setUp() {
		projectRepository = mock(ProjectRepository.class);
		employeeRepository = mock(EmployeeRepository.class);
		service = new StatisticsServiceImpl();
		service.setEmployeeRepository(employeeRepository);
		service.setProjectRepository(projectRepository);
	}

	@Test
	public void getEmployeeTimeOnProjectNoData() {
		Long employeeId = 123214L;
		Employee empl = new Employee();
		when(employeeRepository.getEmployeeAndLogbookEntries(employeeId)).thenReturn(empl);

		Map<Project, Long> result = service.getEmployeeTimeOnProject(employeeId);

		assertEquals(0, result.size());
	}

	@Test
	public void getEmployeeTimeOnSprintNoData() {
		Long employeeId = 23423L;
		Employee e = new Employee();
		when(employeeRepository.getEmployeeAndLogbookEntries(employeeId)).thenReturn(e);

		Map<Project, Map<Sprint, Long>> result = service.getEmployeeTimeOnSprint(employeeId);

		assertEquals(0, result.size());
	}

	@Test
	public void getEmployeeTimeOnProjectPerEmployeeNoData() {
		Long projectId = 90820L;
		Project p = new Project();
		when(projectRepository.findAndLoadLogbookEntries(projectId)).thenReturn(p);

		Map<Employee, Long> result = service.getEmployeeTimeOnProjectPerEmployee(projectId);

		assertEquals(0, result.size());
	}

	@Test
	public void getSprintTimeNoData() {
		Long projectId = 154256L;
		Project p = new Project();
		when(projectRepository.findAndLoadLogbookEntries(projectId)).thenReturn(p);

		Map<Sprint, Long> result = service.getSprintTime(projectId);

		assertEquals(0, result.size());
	}

	@Test
	public void getRequirementTimeNoData() {
		Long projectId = 594023L;
		Project p = new Project();
		when(projectRepository.findAndLoadLogbookEntries(projectId)).thenReturn(p);

		Map<Requirement, Long> result = service.getRequirementTime(projectId);

		assertEquals(0, result.size());
	}
}
