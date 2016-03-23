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
package at.christophwurst.orm.test;

import at.christophwurst.orm.config.IntegrationalTestsConfig;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationalTestsConfig.class, loader = AnnotationConfigContextLoader.class)
public class IntegrationTest {
	
	private static DbSetup dbSetup;

	@BeforeClass
	public static void setUpClass() {
		Operation operation = DbOperations.PREPARE_DB;
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.apache.derby.jdbc.ClientDriver");
		dataSource.setUrl("jdbc:derby://localhost/WorkLogDb;create=true");
		dataSource.setUsername("user");
		dataSource.setPassword("test");
		dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
	}

	@Before
	public void setUp() {
		dbSetup.launch();
	}
	
	
}