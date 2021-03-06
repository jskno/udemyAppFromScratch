package com.library.app.common.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public abstract class GenericRepository<T> {

	protected abstract Class<T> getPersistentClass();

	protected abstract EntityManager getEntityManager();

	public T add(final T entity) {
		getEntityManager().persist(entity);
		return entity;
	}

	public T findById(final Long id) {
		if (id == null) {
			return null;
		}
		return getEntityManager().find(getPersistentClass(), id);
	}

	public void update(final T entity) {
		getEntityManager().merge(entity);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(final String orderField) {
		return getEntityManager()
				.createQuery("Select e From " + getPersistentClass().getSimpleName() + " e Order By e." + orderField)
				.getResultList();
	}

	public boolean alreadyExists(final String propertyName, final String propertyValue, final Long id) {
		final StringBuilder jpql = new StringBuilder();
		jpql.append("Select 1 From " + getPersistentClass().getSimpleName() + " e Where e." + propertyName
				+ "= :propertyValue");
		if (id != null) {
			jpql.append(" And e.id != :id");
		}

		final Query query = getEntityManager().createQuery(jpql.toString());
		query.setParameter("propertyValue", propertyValue);
		if (id != null) {
			query.setParameter("id", id);
		}

		return query.setMaxResults(1).getResultList().size() > 0;
	}

	public boolean existsById(final Long id) {

		// return getEntityManager().find(getPersistentClass(), id) == null;

		// final String jpql = "Select 1 From +
		// getPersistentClass().getSimpleName() + " e Where e.id = :id";
		// final Query query = getEntityManager().createQuery(jpql);
		// query.setParameter("id", id);
		// return query.setMaxResults(1).getResultList().size() > 0;

		return getEntityManager()
				.createQuery("Select 1 From " + getPersistentClass().getSimpleName() + " e Where e.id = :id")
				.setParameter("id", id)
				.setMaxResults(1)
				.getResultList().size() > 0;

	}

}
