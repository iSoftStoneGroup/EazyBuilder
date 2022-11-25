package com.eazybuilder.dm.dao;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

//import org.springframework.data.querydsl.QueryDslPredicateExecutor;

@NoRepositoryBean
public interface BaseDao<T, ID extends Serializable>
        extends PagingAndSortingRepository<T, ID>,QuerydslPredicateExecutor<T>{


}

