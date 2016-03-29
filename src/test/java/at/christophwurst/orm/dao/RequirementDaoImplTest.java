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

import at.christophwurst.orm.domain.Requirement;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class RequirementDaoImplTest extends DaoTest<Requirement> {

	public RequirementDaoImplTest() {
		super(Requirement.class);
	}

	@Override
	protected Dao<Requirement> getDao() {
		return new RequirementDaoImpl();
	}

	@Override
	protected void prepareData() {
		elem1 = new Requirement("Req 1");
		elem2 = new Requirement("Req 2");
	}

	@Test
	public void find() {
		List<Requirement> all = dao.getAll();
		assertEquals(2, all.size());

		Requirement found = dao.getById(all.get(0).getId());
		assertNotNull(found);
		Requirement notFound = dao.getById(0L);
		assertNull(notFound);
	}

}