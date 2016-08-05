package com.library.app.commontests.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestBaseRepository {
	private EntityManagerFactory emf;
	protected EntityManager em;
	protected DBCommandTransactionalExecutor dbCommandExecutor;

	protected void initializeTestDV() {
		emf = Persistence.createEntityManagerFactory("libraryPU");
		em = emf.createEntityManager();

		dbCommandExecutor = new DBCommandTransactionalExecutor(em);
	}

	protected void closeEntityManager() {
		em.close();
		emf.close();
	}
}
