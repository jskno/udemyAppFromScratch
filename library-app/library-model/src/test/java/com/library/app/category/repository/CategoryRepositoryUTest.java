package com.library.app.category.repository;

import static com.library.app.commontests.category.CategoryForTestsRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.library.app.category.model.Category;
import com.library.app.commontests.utils.TestBaseRepository;

public class CategoryRepositoryUTest extends TestBaseRepository {
	private CategoryRepository categoryRepository;

	@Before
	public void initTestCase() {
		initializeTestDV();
		categoryRepository = new CategoryRepository();
		categoryRepository.em = em;
	}

	@After
	public void setDownTestCase() {
		closeEntityManager();
	}

	@Test
	public void addCategoryAndFindIt() {

		// FIRST VERSION

		// Long categoryAddedId = null;
		// try {
		// em.getTransaction().begin();
		// categoryAddedId = categoryRepository.add(java()).getId();
		// assertThat(categoryAddedId, is(notNullValue()));
		// em.getTransaction().commit();
		// em.clear();
		// } catch (final Exception e) {
		// fail("This exception should have not been thrown");
		// e.printStackTrace();
		// em.getTransaction().rollback();
		// }

		// SECOND VERSION REFACTORISED

		// final Long categoryAddedId =
		// dBCommandTransactionalExecutor.executeCommand(new DBCommand<Long>() {
		//
		// @Override
		// public final Long execute() {
		// return categoryRepository.add(java()).getId();
		// }
		//
		// });

		// THIRD VERSION USING LAMBDA EXPRESSIONS

		final Long categoryAddedId = dbCommandExecutor.executeCommand(() -> {
			return categoryRepository.add(java()).getId();
		});

		final Category category = categoryRepository.findById(categoryAddedId);
		assertThat(category, is(notNullValue()));
		assertThat(category.getName(), is(equalTo(java().getName())));
	}

	@Test
	public void findCategoryByIdNotFound() {
		final Category category = categoryRepository.findById(999L);
		assertThat(category, is(nullValue()));
	}

	@Test
	public void findCategoryByIdWithNullId() {
		final Category category = categoryRepository.findById(null);
		assertThat(category, is(nullValue()));
	}

	@Test
	public void updateCategory() {
		final Long categoryAddedId = dbCommandExecutor.executeCommand(() -> {
			return categoryRepository.add(java()).getId();
		});

		final Category categoryAfterAdd = categoryRepository.findById(categoryAddedId);
		assertThat(categoryAfterAdd.getName(), is(equalTo(java().getName())));

		categoryAfterAdd.setName(cleanCode().getName());
		dbCommandExecutor.executeCommand(() -> {
			categoryRepository.update(categoryAfterAdd);
			return null;
		});

		final Category categoryAfterUpdate = categoryRepository.findById(categoryAddedId);
		assertThat(categoryAfterUpdate.getName(), is(equalTo(cleanCode().getName())));
	}

	@Test
	public void findAllCategories() {
		dbCommandExecutor.executeCommand(() -> {

			// FIRST VERSION

			// for (final Category category : allCategories()) {
			// categoryRepository.add(category);
			// }

			// SECOND VERSION

			allCategories().forEach(categoryRepository::add);
			return null;
		});

		final List<Category> categories = categoryRepository.findAll("name");
		assertThat(categories.size(), is(equalTo(4)));
		assertThat(categories.get(0).getName(), is(equalTo(architecture().getName())));
		assertThat(categories.get(1).getName(), is(equalTo(cleanCode().getName())));
		assertThat(categories.get(2).getName(), is(equalTo(java().getName())));
		assertThat(categories.get(3).getName(), is(equalTo(networks().getName())));
	}

	@Test
	public void alreadyExistsForAdd() {
		dbCommandExecutor.executeCommand(() -> {
			categoryRepository.add(java());
			return null;
		});

		assertThat(categoryRepository.alreadyExists(java()), is(equalTo(true)));
		assertThat(categoryRepository.alreadyExists(cleanCode()), is(equalTo(false)));
	}

	@Test
	public void alreadyExistsWithCategoryId() {
		final Category java = dbCommandExecutor.executeCommand(() -> {
			categoryRepository.add(cleanCode());
			return categoryRepository.add(java());
		});

		assertThat(categoryRepository.alreadyExists(java), is(equalTo(false)));

		java.setName(cleanCode().getName());
		assertThat(categoryRepository.alreadyExists(java), is(equalTo(true)));

		java.setName(networks().getName());
		assertThat(categoryRepository.alreadyExists(java), is(equalTo(false)));
	}

	@Test
	public void exitsById() {
		final Long categoryAddedId = dbCommandExecutor.executeCommand(() -> {
			return categoryRepository.add(java()).getId();
		});

		assertThat(categoryRepository.existsById(categoryAddedId), is(equalTo(true)));
		assertThat(categoryRepository.existsById(999L), is(equalTo(false)));

	}

}
