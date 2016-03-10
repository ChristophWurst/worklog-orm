package swt6.orm.jpa;

import java.text.DateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import swt6.orm.domain.annotated.Address;
import swt6.orm.domain.annotated.Employee;
import swt6.orm.domain.annotated.LogbookEntry;
import swt6.orm.domain.annotated.PermanentEmployee;
import swt6.orm.domain.annotated.Project;
import swt6.orm.domain.annotated.TemporaryEmployee;
import swt6.util.DateUtil;

public class JPAWorkLogManager {

	private static final DateFormat fmt = DateFormat.getDateTimeInstance();

	private static Long saveEmployee(Employee empl) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		em.persist(empl);
		JPAUtil.commit();
		return empl.getId();
	}

	private static void getEmployee(long emplId) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		Employee empl = em.find(Employee.class, emplId);
		if (empl != null) {
			System.out.println(empl);
		}
		JPAUtil.commit();
	}

	private static void addLogbookEntries(Employee empl) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		empl = em.merge(empl);
		LogbookEntry entry = new LogbookEntry("Analyse", DateUtil.getTime(10, 15), DateUtil.getTime(15, 30));
		empl.addLogbookEntry(entry);
		LogbookEntry entry2 = new LogbookEntry("Implementierung", DateUtil.getTime(8, 45), DateUtil.getTime(17, 15));
		empl.addLogbookEntry(entry2);
		JPAUtil.commit();
	}

	private static void listEmployees() {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		List<Employee> emplList = em.createQuery("select e from Employee e", Employee.class).getResultList();
		for (Employee e : emplList) {
			System.out.println(e);
			if (e.getLogbookEntries().size() > 0) {
				for (LogbookEntry entry : e.getLogbookEntries()) {
					System.out.println("    " + entry);
				}
			}
			if (e.getProjects().size() > 0) {
				for (Project p : e.getProjects()) {
					System.out.println("   " + p);
				}
			}
		}
		JPAUtil.commit();
	}

	private static void getLogbookEntry(long entryId) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		LogbookEntry entry = em.find(LogbookEntry.class, entryId);
		if (entry != null) {
			System.out.println(entry);
		}
		JPAUtil.commit();
	}

	private static void listLogbokEntries(Employee empl) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		TypedQuery<LogbookEntry> qry = em.createQuery("from LogbookEntry where employee = :empl", LogbookEntry.class);
		qry.setParameter("empl", empl);
		List<LogbookEntry> entries = qry.getResultList();
		for (LogbookEntry entry : entries) {
			System.out.print(entry);
		}
		JPAUtil.commit();
	}

	private static void assignProjectsToEmployees(Employee empl1) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		empl1 = em.merge(empl1);
		Project p = new Project("Office");
		empl1.addProject(p);
		JPAUtil.commit();
	}

	public static void main(String[] args) {
		System.out.println("----- create schema -----");
		JPAUtil.getEntityManager();

		PermanentEmployee empl1 = new PermanentEmployee("Franz", "Mayr", DateUtil.getDate(1980, 12, 24));
		empl1.setAddress(new Address("4232", "Hagenberg", "Hauptstraße 1"));
		empl1.setSalary(5000.0);

		TemporaryEmployee empl2 = new TemporaryEmployee("Bill", "Gates", DateUtil.getDate(1970, 1, 21));
		empl2.setAddress(new Address("77777", "Redmond", "Clinton Street"));
		empl2.setHourlyRate(50.0);
		empl2.setRenter("Microsoft");
		empl2.setStartDate(DateUtil.getDate(2006, 3, 1));
		empl2.setEndDate(DateUtil.getDate(2006, 4, 1));

		try {
			System.out.println("--- saveEmployee ---");
			saveEmployee(empl1);

			System.out.println("--- saveEmployee ---");
			saveEmployee(empl2);

			System.out.println("--- getEmployee ---");
			getEmployee(empl1.getId());

			System.out.println("--- addLogbookEntry ---");
			addLogbookEntries(empl1);

			System.out.println("--- listEmployees ---");
			listEmployees();

			System.out.println("--- listLogbookEntries ---");
			listLogbokEntries(empl1);

			System.out.println("--- assignProjectsToEmployee ---");
			assignProjectsToEmployees(empl1);

			System.out.println("--- listEmployees ---");
			listEmployees();
		} finally {
			JPAUtil.closeEntityManagerFactory();
		}

	}

}
