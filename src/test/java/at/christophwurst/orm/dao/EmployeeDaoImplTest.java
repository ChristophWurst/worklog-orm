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
package at.christophwurst.orm.dao;

import at.christophwurst.orm.domain.Employee;
import at.christophwurst.orm.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class EmployeeDaoImplTest extends DaoTest<Employee> {

	public EmployeeDaoImplTest() {
		super(Employee.class);
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		dao = new EmployeeDaoImpl();
	}

	@Test
	public void getAll() {
		dao.getAll();
	}

	@Test
	public void getById() {
		Long id = 1234L;

		Employee e = dao.getById(id);

		assertNotNull(e);
	}

	@Test
	public void getByIdNotFound() {
		Employee e = dao.getById(3000L);

		assertNull(e);
	}

	@Override
	protected void prepareData() {
		elem1 = new Employee("Jane", "Doe", DateUtil.getDate(1980, 05, 06));
		elem2 = new Employee("John", "Doe", DateUtil.getDate(1970, 4, 5));
	}
}
