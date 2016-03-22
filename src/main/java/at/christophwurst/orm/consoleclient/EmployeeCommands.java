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
package at.christophwurst.orm.consoleclient;

import at.christophwurst.orm.domain.Employee;
import at.christophwurst.orm.service.EmployeeService;
import at.christophwurst.orm.util.DateUtil;
import java.util.Date;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
@Component
public class EmployeeCommands {

	@Inject
	private EmployeeService employeeService;

	public void registerCommands(Client client) {
		client.registerCommand("employee:list", (consoleInterface) -> {
			employeeService.getAll().stream().forEach((empl) -> {
				System.out.println(" - " + empl.getId() + ": " + empl);
			});
		});

		client.registerCommand("employee:show", (consoleInterface) -> {
			Long id = consoleInterface.getLongValue("employee id");
			System.out.println(employeeService.getById(id));
		});

		client.registerCommand("employee:create", (consoleInterface) -> {
			String firstName = consoleInterface.getStringValue("firstname");
			String lastName = consoleInterface.getStringValue("lastname");
			int year = consoleInterface.getIntValue("birth year");
			int month = consoleInterface.getIntValue("birth month");
			int day = consoleInterface.getIntValue("birth day");
			Date dob = DateUtil.getDate(year, month, day);

			Employee employee = new Employee(firstName, lastName, dob);

			employeeService.save(employee);
		});

		client.registerCommand("employee:update", (consoleInterface) -> {
			Long id = consoleInterface.getLongValue("employee id");
			Employee employee = employeeService.getById(id);

			String firstName = consoleInterface.getStringValue("firstname");
			String lastName = consoleInterface.getStringValue("lastname");
			int year = consoleInterface.getIntValue("birth year");
			int month = consoleInterface.getIntValue("birth month");
			int day = consoleInterface.getIntValue("birth day");
			Date dob = DateUtil.getDate(year, month, day);

			employee.setFirstName(firstName);
			employee.setLastName(lastName);
			employee.setDateOfBirth(dob);

			employeeService.save(employee);
		});
	}

}
