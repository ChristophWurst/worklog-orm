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
import at.christophwurst.orm.util.JPAUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;

public class SprintDaoImpl implements SprintDao {

	@Override
	public Sprint getSprintAndWorklogs(Sprint sprint) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		EntityGraph graph = em.getEntityGraph("graph.Sprint.logbookEntries");

		Map hints = new HashMap();
		hints.put("javax.persistence.fetchgraph", graph);

		sprint = em.merge(sprint);
		sprint = em.createQuery("select s from Sprint s where s = :s", Sprint.class)
			.setParameter("s", sprint)
			.setHint("javax.persistence.fetchgraph", graph)
			.getSingleResult();

		JPAUtil.commit();
		return sprint;
	}

	@Override
	public List<Sprint> getAll() {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		List<Sprint> sprints = em.createQuery("from Sprint", Sprint.class).getResultList();
		JPAUtil.commit();
		return sprints;
	}

}
