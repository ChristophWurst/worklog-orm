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
import at.christophwurst.orm.domain.LogbookEntry;
import at.christophwurst.orm.util.DateUtil;
import org.junit.Test;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class LogbookEntryDaoImplTest extends DaoTest<LogbookEntry> {

	public LogbookEntryDaoImplTest() {
		super(LogbookEntry.class);
	}

	@Override
	protected Dao<LogbookEntry> getDao() {
		return new LogbookEntryDaoImpl();
	}

	@Override
	protected void prepareData() {
		elem1 = new LogbookEntry("Log 1", DateUtil.getTime(2016, 3, 14, 8, 30), DateUtil.getTime(2016, 3, 14, 10, 0));
		elem1.setEmployee(new Employee());
		elem2 = new LogbookEntry("Log 2", DateUtil.getTime(2016, 3, 14, 10, 30), DateUtil.getTime(2016, 3, 14, 12, 0));
		elem2.setEmployee(new Employee());
	}
	
	@Test
	public void find() {
		
	}
	
}
