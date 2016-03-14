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
import javax.persistence.EntityManager;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
abstract class DaoImpl<T> implements Dao<T> {

	private final Class<T> clazz;

	public DaoImpl(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public List<T> getAll() {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		List<T> result = em.createQuery("from " + clazz.getName(), clazz).getResultList();
		JPAUtil.commit();
		return result;
	}

	@Override
	public T getById(Long id) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		T elem = em.find(clazz, id);
		JPAUtil.commit();
		return elem;
	}

	@Override
	public void saveOrUpdate(T t) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		em.merge(t); // TODO: merge?
		JPAUtil.commit();
	}

	@Override
	public void delete(T t) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		em.remove(t);
		JPAUtil.commit();
	}

}
