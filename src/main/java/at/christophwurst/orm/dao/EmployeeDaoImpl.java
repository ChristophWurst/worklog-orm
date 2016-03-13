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
import java.util.List;
import at.christophwurst.orm.domain.Employee;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
class EmployeeDaoImpl implements EmployeeDao {

	@Override
	public List<Employee> getAll() {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		List<Employee> empls = em.createQuery("from Employee", Employee.class).getResultList();
		JPAUtil.commit();
		return empls;
	}

	@Override
	public Employee getById(Long id) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		Employee empl = em.find(Employee.class, id);
		JPAUtil.commit();
		return empl;
	}

	@Override
	public void save(Employee employee) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		em.persist(employee);
		JPAUtil.commit();
	}

	@Override
	public List<Employee> getEmployeesAndLogbookEntries() {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		EntityGraph graph = em.getEntityGraph("graph.Employee.logbookEntries");

		Map hints = new HashMap();
		hints.put("javax.persistence.fetchgraph", graph);

		List<Employee> projects = em.createQuery("from Employee", Employee.class)
			.setHint("javax.persistence.fetchgraph", graph)
			.getResultList();

		Set<Employee> noDupes = new HashSet<>(projects);
		projects.clear();
		projects.addAll(noDupes);

		JPAUtil.commit();
		return projects;
	}

}
