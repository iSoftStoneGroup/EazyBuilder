package com.eazybuilder.ci.repository;

import java.io.Serializable;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
//import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface BaseDao<T, ID extends Serializable>
        extends PagingAndSortingRepository<T, ID>,QuerydslPredicateExecutor<T>{


}

