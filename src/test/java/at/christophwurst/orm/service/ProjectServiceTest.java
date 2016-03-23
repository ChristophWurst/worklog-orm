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

import at.christophwurst.orm.config.IntegrationalTestsConfig;
import at.christophwurst.orm.domain.Project;
import at.christophwurst.orm.domain.Requirement;
import at.christophwurst.orm.domain.Task;
import at.christophwurst.orm.test.IntegrationTest;
import javax.inject.Inject;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationalTestsConfig.class, loader = AnnotationConfigContextLoader.class)
public class ProjectServiceTest extends IntegrationTest {

	@Inject
	private ProjectService service;

	@Test
	public void getAll() {
		assertEquals(2, service.getAllProjects().size());
	}

	@Test
	public void getProjectById() {
		Project p = service.getProjectById(100L);
		assertNotNull(p);
	}

	@Test
	public void getProjectByIdNotFound() {
		Project p = service.getProjectById(666L);
		assertNull(p);
	}

	@Test
	public void saveNewProject() {
		Project p = new Project("Super project");
		p = service.saveProject(p);

		Project fromDb = service.getProjectById(p.getId());
		assertEquals("Super project", fromDb.getName());
	}

	@Test
	public void updateExistingProject() {
		Project existing = service.getProjectById(100L);
		existing.setName("New project name");

		service.saveProject(existing);

		Project fromDb = service.getProjectById(100L);
		assertEquals("New project name", fromDb.getName());
	}

	@Test
	public void getRequirementById() {
		Requirement r = service.getRequirementById(200L);
		assertNotNull(r);
	}

	@Test
	public void getRequirementByIdNotFound() {
		Requirement r = service.getRequirementById(666L);
		assertNull(r);
	}

	@Test
	public void getTaskById() {
		Task t = service.getTaskById(300L);
		assertNotNull(t);
	}

	@Test
	public void getTaskByIdNotFound() {
		Task t = service.getTaskById(666L);
		assertNull(t);
	}

}
