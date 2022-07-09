package com.eazybuilder.ci.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.Predicate;

public interface CommonService<T> {

	public Iterable<T> findAll(Predicate condition);
	
	public Iterable<T> findAll();

	public void save(List<T> entitys);
	
	public void save(T entity);

	public T findOne(String id);

	public void deleteBatch(List<String> ids);

	public void delete(String id);
	
	public Page<T> pageSearch(Pageable pageable,String searchText);
}
