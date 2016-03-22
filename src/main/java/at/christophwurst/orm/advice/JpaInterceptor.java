package at.christophwurst.orm.advice;

import javax.persistence.EntityManagerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import at.christophwurst.orm.util.JPAUtil;
import javax.inject.Inject;
import javax.inject.Named;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class JpaInterceptor {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Inject
	@Named("entityManagerFactory")
	private EntityManagerFactory entityManagerFactory;

	protected EntityManagerFactory getEntityManagerFactory() {
		return this.entityManagerFactory;
	}

	public void setEntityManagerFactory(EntityManagerFactory emFactory) {
		System.out.println("--------------------------------------------------------");
		this.entityManagerFactory = emFactory;
	}

	@Around("execution(* at.christophwurst.orm.consoleclient.CommandExecutor.execute(..))")
	public Object holdEntityManger(ProceedingJoinPoint pjp) throws Throwable {
		if (entityManagerFactory == null) {
			throw new IllegalArgumentException("Property 'entityManagerFactory' is required");
		}

		boolean participate = false;
		if (TransactionSynchronizationManager.hasResource(entityManagerFactory)) {
			participate = true;
		} else {
			logger.trace("Opening EntityManager");
			JPAUtil.openEntityManager(entityManagerFactory);
		}

		try {
			return pjp.proceed(); // delegates to method of target class.
		} finally {
			if (!participate) {
				JPAUtil.closeEntityManager(entityManagerFactory);
				logger.trace("Closed EntityManager");
			}
		}
	}
}
