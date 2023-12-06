package com.Shop.Service.Generic;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public class GenericService<T> implements IGenericService<T> {

	protected final JpaRepository<T, Long> genericRepository;

	public GenericService(JpaRepository<T, Long> gmRepository) {
		this.genericRepository = gmRepository;
	}

	@Override
	public List<T> findAll() throws Exception {
		try {
			return genericRepository.findAll();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public T save(T entity) throws Exception {
		try {
			return genericRepository.save(entity);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void delete(Long id) throws Exception {
		try {
			genericRepository.deleteById(id);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Optional<T> findById(Long id) throws Exception {
		try {
			return (Optional<T>) genericRepository.findById(id);
		} catch (Exception e) {
			throw e;
		}
	}

}
