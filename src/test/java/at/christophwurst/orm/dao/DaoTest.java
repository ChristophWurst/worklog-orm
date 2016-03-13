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

import at.christophwurst.orm.util.JPAUtil;
import com.ninja_squad.dbsetup.DbSetup;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.apache.derby.jdbc.ClientDataSource;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public abstract class DaoTest {

	private static DbSetup dbSetup;

	@BeforeClass
	public static void setUpClass() {
		JPAUtil.getEntityManager();

		Operation operation = sequenceOf(DbOperations.PREPARE_DB);
		ClientDataSource ds = new ClientDataSource();
		ds.setServerName("localhost");
		ds.setDatabaseName("WorkLogDb");
		dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
	}

	@Before
	public void setUp() {
		dbSetup.launch();
	}

}