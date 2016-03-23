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
import at.christophwurst.orm.test.IntegrationTest;
import java.util.Date;
import javax.inject.Inject;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class EmployeeServiceTest extends IntegrationTest {

	@Inject
	private EmployeeService service;

	@Test
	public void getAll() {
		assertEquals(2, service.getAll().size());
	}

	@Test
	public void getById() {
		Employee empl1234 = service.getById(1234L);
		assertNotNull(empl1234);
		assertEquals(new Long(1234), empl1234.getId());
	}

	@Test
	public void saveNew() {
		Employee newEmpl = new Employee("Fritz", "Phantom", new Date());
		service.save(newEmpl);

		assertEquals(3, service.getAll().size());
	}

	@Test
	public void updateExisting() {
		Employee existingEmployee = service.getById(1235L);
		existingEmployee.setLastName("Huber");

		service.save(existingEmployee);

		Employee fromDb = service.getById(1235L);
		assertEquals("Huber", fromDb.getLastName());
	}

}
