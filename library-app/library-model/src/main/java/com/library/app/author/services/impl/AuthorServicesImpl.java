package com.library.app.author.services.impl;

import com.library.app.author.exception.AuthorNotFoundException;
import com.library.app.author.model.Author;
import com.library.app.author.model.filter.AuthorFilter;
import com.library.app.author.repository.AuthorRepository;
import com.library.app.author.services.AuthorServices;
import com.library.app.common.model.PaginatedData;
import com.library.app.common.utils.ValidationUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.List;

@Stateless
public class AuthorServicesImpl implements AuthorServices {

	@Inject
	Validator validator;

	@Inject
	AuthorRepository authorRepository;

	@Override
	public Author add(final Author author) {
		ValidationUtils.validateEntityFields(validator, author);
		return authorRepository.add(author);
	}

	@Override
	public void update(final Author author) {
		ValidationUtils.validateEntityFields(validator, author);

		if (!authorRepository.existsById(author.getId())) {
			throw new AuthorNotFoundException();
		}

		authorRepository.update(author);
	}

	@Override
	public Author findById(final Long id) {
		final Author author = authorRepository.findById(id);
		if (author == null) {
			throw new AuthorNotFoundException();
		}
		return author;
	}

	@Override
	public PaginatedData<Author> findByFilter(final AuthorFilter authorFilter) {
		return authorRepository.findByFilter(authorFilter);
	}

	@Override
	public List<Author> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
