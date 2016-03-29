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

import at.christophwurst.orm.dao.DatabaseFactory;
import at.christophwurst.orm.dao.EmployeeDao;
import at.christophwurst.orm.dao.ProjectDao;
import at.christophwurst.orm.dao.SprintDao;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class ServiceContainer {

	private static final ProjectDao projectDao;
	private static final EmployeeDao emplyoeeDao;
	private static final SprintDao sprintDao;

	static {
		projectDao = DatabaseFactory.getProjectDao();
		emplyoeeDao = DatabaseFactory.getEmployeeDao();
		sprintDao = DatabaseFactory.getSprintDao();
	}

	public static ProjectService getProjectService() {
		return new ProjectServiceImpl(projectDao);
	}

	public static StatisticsService getStatisticsService() {
		return new StatisticsServiceImpl(projectDao, emplyoeeDao);
	}

	public static ScrumService getScrumService() {
		return new ScrumServiceImpl(sprintDao);
	}

	public static BurnDownService getBurnDownService() {
		return new BurnDownServiceImpl(sprintDao);
	}

}