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
import at.christophwurst.orm.domain.Employee;
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
public class EmployeeServiceImplTest {

	private EmployeeRepository employeeRepository;
	private EmployeeServiceImpl employeeService;

	@Before
	public void setUp() {
		employeeRepository = mock(EmployeeRepository.class);
		employeeService = new EmployeeServiceImpl();
		employeeService.setEmployeeRepository(employeeRepository);
	}

	@Test
	public void getAll() {
		List<Employee> expected = new ArrayList<>();
		expected.add(new Employee());
		expected.add(new Employee());
		when(employeeRepository.findAll()).thenReturn(expected);

		assertSame(expected, employeeService.getAll());
	}

	@Test
	public void getById() {
		Long id = 124L;
		Employee expected = new Employee();
		when(employeeRepository.findOne(id)).thenReturn(expected);

		assertSame(expected, employeeService.getById(id));
	}

	@Test
	public void getByIdNotFound() {
		Long id = 124L;
		when(employeeRepository.findOne(id)).thenReturn(null);

		assertNull(employeeService.getById(id));
	}

	@Test
	public void save() {
		Employee e = new Employee();
		Employee saved = new Employee();
		when(employeeRepository.save(e)).thenReturn(saved);

		assertSame(saved, employeeService.save(e));
	}

}
