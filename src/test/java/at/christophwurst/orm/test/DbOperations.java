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

import static com.ninja_squad.dbsetup.Operations.*;
import com.ninja_squad.dbsetup.operation.Operation;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public abstract class DbOperations {

	public static final Operation PREPARE_DB;

	static {
		Operation clearDb = deleteAllFrom("Project_Employee", "Employee", "Address", "Task", "Requirement", "Project", "LogbookEntry", "Sprint");
		Operation insertAddressData = insertInto("Address")
			.columns("id", "city", "street", "zipCode")
			.values(1, "Hollabrunn", "Pfarrgasse", "2020")
			.values(2, "Schrattenthal", "Obermarkersdorf", "2073")
			.build();
		Operation insertEmployeeData = insertInto("Employee")
			.columns("id", "dateOfBirth", "firstName", "lastName", "address_id")
			.values(1234, "1992-05-06", "John", "Doe", 1)
			.values(1235, "1992-01-06", "Jane", "Doe", 2)
			.build();
		Operation insertProjectData = insertInto("Project")
			.columns("id", "name")
			.values(100, "Project 1")
			.values(101, "Project 2")
			.build();
		Operation insertProjectEmployeeData = insertInto("Project_Employee")
			.columns("projects_id", "employees_id")
			.values(100, 1234)
			.values(100, 1235)
			.values(101, 1234)
			.build();
		Operation insertSprintData = insertInto("Sprint")
			.columns("id", "nr")
			.values(1, 1)
			.values(2, 2)
			.build();
		Operation insertRequirementData = insertInto("Requirement")
			.columns("id", "shortDesc", "project_id")
			.values(200, "Req 1", 100)
			.values(201, "Req 2", 100)
			.build();
		Operation insertTaskData = insertInto("Task")
			.columns("id", "shortDescription", "estimatedTime", "requirement_id")
			.values(300, "Tsk 1", 3, 200)
			.values(301, "Tsk 2", 5, 200)
			.build();

		PREPARE_DB = sequenceOf(
			clearDb,
			insertAddressData,
			insertEmployeeData,
			insertProjectData,
			insertProjectEmployeeData,
			insertSprintData,
			insertRequirementData,
			insertTaskData);
	}
}
