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

import at.christophwurst.orm.domain.Task;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class TaskDaoImplTest extends DaoTest<Task> {

	public TaskDaoImplTest() {
		super(Task.class);
	}

	@Override
	protected Dao<Task> getDao() {
		return new TaskDaoImpl();
	}

	@Override
	protected void prepareData() {
		elem1 = new Task("Task 1");
		elem2 = new Task("Task 2");
	}

	@Test
	public void find() {
		List<Task> all = dao.getAll();
		assertEquals(2, all.size());

		Task found = dao.getById(all.get(0).getId());
		assertNotNull(found);
		Task notFound = dao.getById(0L);
		assertNull(notFound);
	}

}
