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
import at.christophwurst.orm.domain.LogbookEntry;
import at.christophwurst.orm.domain.PermanentEmployee;
import at.christophwurst.orm.domain.Project;
import at.christophwurst.orm.domain.Requirement;
import at.christophwurst.orm.domain.Sprint;
import at.christophwurst.orm.domain.Task;
import at.christophwurst.orm.domain.TemporaryEmployee;
import at.christophwurst.orm.util.DateUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import at.christophwurst.orm.dao.ProjectRepository;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
@Component
public class DbSeeder {

	private ProjectRepository projectRepository;

	@Inject
	public void setProjectRepository(ProjectRepository projectRepo) {
		projectRepository = projectRepo;
	}

	public void seed() {
		TemporaryEmployee e1 = new TemporaryEmployee("Jane", "Doe", DateUtil.getDate(1970, 1, 2));
		e1.setStartDate(DateUtil.getDate(2015, 10, 11));
		e1.setEndDate(DateUtil.getDate(2016, 1, 11));
		e1.setHourlyRate(70);
		PermanentEmployee e2 = new PermanentEmployee("John", "Doe", DateUtil.getDate(1970, 3, 4));
		e2.setSalary(5000);
		List<Employee> empls = new ArrayList<>();
		empls.add(e1);
		empls.add(e2);
		List<Sprint> sprints1 = new ArrayList<>();
		sprints1.add(new Sprint(1));
		sprints1.add(new Sprint(2));
		sprints1.add(new Sprint(3));
		Requirement r11 = new Requirement("Reqirement 1-1");
		sprints1.get(0).addRequirement(r11);
		Requirement r12 = new Requirement("Reqirement 1-2");
		sprints1.get(1).addRequirement(r12);
		Requirement r13 = new Requirement("Reqirement 1-3");
		sprints1.get(2).addRequirement(r13);
		Task t111 = new Task("Design 11");
		t111.setEstimatedTime(2);
		Task t112 = new Task("Implemnt 11");
		t112.setEstimatedTime(1);
		Task t113 = new Task("Test 11");
		t113.setEstimatedTime(2);
		Task t121 = new Task("Design 12");
		t121.setEstimatedTime(1);
		Task t122 = new Task("Implemnt 12");
		t122.setEstimatedTime(1);
		Task t131 = new Task("Design 13");
		t131.setEstimatedTime(3);
		List<Task> tasks = new ArrayList();
		tasks.add(t111);
		tasks.add(t112);
		tasks.add(t113);
		tasks.add(t121);
		tasks.add(t122);
		tasks.add(t131);
		tasks.forEach((Task t) -> {
			Random r = new Random();
			LogbookEntry lbe1 = new LogbookEntry("Design modules", DateUtil.getTime(2015, 12, 14, 8, 0), DateUtil.getTime(2015, 12, 14, 9, 0));
			lbe1.setEmployee(empls.get(r.nextInt(empls.size())));
			LogbookEntry lbe2 = new LogbookEntry("Design interfaces", DateUtil.getTime(2015, 12, 15, 9, 0), DateUtil.getTime(2015, 12, 15, 10, 0));
			lbe2.setEmployee(empls.get(r.nextInt(empls.size())));
			LogbookEntry lbe3 = new LogbookEntry("Document interfaces", DateUtil.getTime(2015, 12, 16, 10, 0), DateUtil.getTime(2015, 12, 16, 11, 0));
			lbe3.setEmployee(empls.get(r.nextInt(empls.size())));
			t.addLogbookEntry(lbe1);
			t.addLogbookEntry(lbe2);
			t.addLogbookEntry(lbe3);
		});
		r11.addTask(t111);
		r11.addTask(t112);
		r11.addTask(t113);
		r12.addTask(t121);
		r12.addTask(t122);
		r13.addTask(t131);

		Project p1 = new Project("Project A");
		p1.addMember(e1);
		p1.addMember(e2);
		p1.addRequirement(r11);
		p1.addRequirement(r12);
		p1.addRequirement(r13);
		Project p2 = new Project("Project B");
		p2.addSprint(new Sprint(1));
		p2.addSprint(new Sprint(2));
		p2.addSprint(new Sprint(3));
		p2.addMember(e2);

		projectRepository.save(p1);
		projectRepository.save(p2);
	}

}
