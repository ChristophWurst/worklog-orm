package at.christophwurst.orm.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class JPAUtil {

	private static final Logger LOG = LoggerFactory.getLogger(JPAUtil.class);

	public static void beginTransaction(EntityManagerFactory entityManagerFactory) {
		EntityManager em = getEntityManager(entityManagerFactory);
		if (em == null) {
			em = openEntityManager(entityManagerFactory);
		}
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
			LOG.trace("Begin Transaction");
		}
	}

	public static void commitTransaction(EntityManagerFactory entityManagerFactory) {
		commitTransaction(entityManagerFactory, true);
	}

	public static void commitTransaction(EntityManagerFactory entityManagerFactory,
		boolean closeEntityManager) {
		EntityManager em = getEntityManager(entityManagerFactory);
		if (em == null) {
			throw new DataAccessResourceFailureException("Invalid commit: No open transaction");
		}
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
			LOG.trace("Committed Transaction");
		}
		if (closeEntityManager) {
			closeEntityManager(entityManagerFactory);
		}
	}

	public static void rollbackTransaction(EntityManagerFactory entityManagerFactory) {
		EntityManager em = getEntityManager(entityManagerFactory);
		if (em == null) {
			throw new DataAccessResourceFailureException("Invalid rollback: No open transaction");
		}
		if (em.getTransaction().isActive()) {
			em.getTransaction().rollback();
			LOG.trace("Rolled back Transaction");
		}
		closeEntityManager(entityManagerFactory);
	}

	public static EntityManager openEntityManager(EntityManagerFactory entityManagerFactory) {
		try {
			EntityManager em = getEntityManager(entityManagerFactory);
			if (em == null) {
				LOG.trace("Opening JPA EntityManager");
				em = entityManagerFactory.createEntityManager();
			}

			TransactionSynchronizationManager.bindResource(entityManagerFactory,
				new EntityManagerHolder(em));
			return em;
		} catch (IllegalStateException ex) {
			throw new DataAccessResourceFailureException("Could not open JPA EntityManager", ex);
		}
	}

	public static void closeEntityManager(EntityManagerFactory entityManagerFactory) {
		try {
			EntityManager em = getEntityManager(entityManagerFactory);
			if (em != null) {
				TransactionSynchronizationManager.unbindResource(entityManagerFactory);
				em.close();
				LOG.trace("Closed JPA EntityManager");
			}
		} catch (IllegalStateException ex) {
			throw new DataAccessResourceFailureException("Could not close JPA EntityManager", ex);
		}
	}

	public static EntityManager getEntityManager(EntityManagerFactory entityManagerFactory) {
		if (entityManagerFactory == null) {
			return null;
		}
		return EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
	}

	public static <T> T getRepositoryForCurrentEntityManager(EntityManagerFactory entityManagerFactory, Class<T> entityType) {
		EntityManager em = getEntityManager(entityManagerFactory);
		if (em.getTransaction().isActive()) {
			throw new DataAccessResourceFailureException("Cannot create repository within transaction");
		}
		JpaRepositoryFactory repoFactory = new JpaRepositoryFactory(em);
		return repoFactory.getRepository(entityType);
	}

}
