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

import at.christophwurst.orm.domain.Sprint;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class SprintDaoImplTest extends DaoTest<Sprint> {

	public SprintDaoImplTest() {
		super(Sprint.class);
	}

	@Override
	protected Dao<Sprint> getDao() {
		return new SprintDaoImpl();
	}

	@Override
	protected void prepareData() {
		elem1 = new Sprint(1);
		elem2 = new Sprint(2);
	}

}