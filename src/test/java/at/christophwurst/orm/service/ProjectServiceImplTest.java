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

import at.christophwurst.orm.dao.ProjectRepository;
import at.christophwurst.orm.dao.RequirementRepository;
import at.christophwurst.orm.dao.TaskRepository;
import at.christophwurst.orm.domain.Project;
import at.christophwurst.orm.domain.Requirement;
import at.christophwurst.orm.domain.Task;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class ProjectServiceImplTest {

	private ProjectRepository projectRepository;
	private RequirementRepository requirementRepository;
	private TaskRepository taskRepository;
	private ProjectServiceImpl service;

	@Before
	public void setUp() {
		projectRepository = mock(ProjectRepository.class);
		requirementRepository = mock(RequirementRepository.class);
		taskRepository = mock(TaskRepository.class);
		service = new ProjectServiceImpl();
		service.setProjectRepository(projectRepository);
		service.setRequirementRepository(requirementRepository);
		service.setTaskRepository(taskRepository);
	}

	@Test
	public void getProjectCostsNoEmployees() {
		Long id = 12356L;
		Project p = new Project();
		when(projectRepository.findAndLoadLogbookEntries(id)).thenReturn(p);

		assertEquals(0.0, service.getProjectCosts(id), 0.0);
	}

	@Test
	public void getAllProjects() {
		List<Project> expected = new ArrayList<>();
		expected.add(new Project());
		expected.add(new Project());
		expected.add(new Project());
		when(projectRepository.findAll()).thenReturn(expected);

		assertSame(expected, service.getAllProjects());
	}

	@Test
	public void getProjectById() {
		Long id = 1232L;
		Project expected = new Project();
		when(projectRepository.findOne(id)).thenReturn(expected);

		assertSame(expected, service.getProjectById(id));
	}

	@Test
	public void getProjectByIdNotFound() {
		Long id = 1232L;
		when(projectRepository.findOne(id)).thenReturn(null);

		assertNull(service.getProjectById(id));
	}

	@Test
	public void saveProject() {
		Project toSave = new Project();
		Project saved = new Project();
		when(projectRepository.save(toSave)).thenReturn(saved);

		assertSame(saved, service.saveProject(toSave));
	}

	@Test
	public void getRequirementById() {
		Long id = 1232L;
		Requirement expected = new Requirement();
		when(requirementRepository.findOne(id)).thenReturn(expected);

		assertSame(expected, service.getRequirementById(id));
	}

	@Test
	public void getRequirementByIdNotFound() {
		Long id = 1232L;
		when(requirementRepository.findOne(id)).thenReturn(null);

		assertNull(service.getRequirementById(id));
	}

	@Test
	public void saveRequirement() {
		Requirement toSave = new Requirement();
		Requirement saved = new Requirement();
		when(requirementRepository.save(toSave)).thenReturn(saved);

		assertSame(saved, service.saveRequirement(toSave));
	}

	@Test
	public void getTaskById() {
		Long id = 1232L;
		Task expected = new Task();
		when(taskRepository.findOne(id)).thenReturn(expected);

		assertSame(expected, service.getTaskById(id));
	}

}
