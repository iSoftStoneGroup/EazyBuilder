package com.eazybuilder.ci.base;

import java.util.List;

import javax.transaction.Transactional;

import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eazybuilder.ci.repository.BaseDao;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.StringPath;

public abstract class AbstractCommonServiceImpl<E extends BaseDao<T,String>,T> implements CommonService<T>{

	@Autowired
	protected E dao;



	@Override
	public Iterable<T> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(T entity) {
		dao.save(entity);
	}

	@Override
	public T findOne(String id) {
		return dao.findById(id).orElse(null);
	}

	@Transactional(rollbackOn=Exception.class)
	@Override
	public void deleteBatch(List<String> ids) {
		for(String id:ids){
			dao.deleteById(id);
		}
	}

	@Override
	public Page<T> pageSearch(Pageable pageable, String searchText) {
		Predicate predicate = null;
		try {
			predicate = createSearchCondition(searchText);
		} catch (Exception e) {
			throw new RuntimeException("构建查询条件search时异常", e);
		}
		if(predicate==null){
			predicate = new BooleanBuilder();
		}
		return dao.findAll(predicate, pageable);
	}


	/**
	 * 从{@link com.eazybuilder.ci.base.PageSearch}
	 * 中获取Q类以及需要查询属性名，创建like语句的predicate，多个以or语句组合
	 *
	 * @param search
	 * @return 该实现类未标记{@link PageSearch}或者fileds为空时返回null
	 * @throws Exception
	 */
	private Predicate createSearchCondition(String search) throws Exception {

		if (StringUtils.isEmpty(search)) {
			return null;
		}

		PageSearch pageSearch = this.getClass().getAnnotation(PageSearch.class);

		if (pageSearch == null) {
			return null;
		}

		Class<? extends EntityPathBase> qClass = pageSearch.value();

		String instanceName = qClass.getSimpleName().substring(1, 2)
				.toLowerCase()
				+ qClass.getSimpleName().substring(2);
		EntityPathBase instance = (EntityPathBase) qClass
				.getField(instanceName).get(null);

		Predicate predicate = null;
		for (String searchField : pageSearch.fields()) {
			StringPath attr = (StringPath) qClass.getField(searchField).get(
					instance);
			if (predicate == null) {
				predicate = attr.like("%" + search + "%");
			} else {
				predicate = ((BooleanExpression) predicate).or(attr.like("%"
						+ search + "%"));
			}
		}

		return predicate;
	}

	@Override
	public void delete(String id) {
		dao.deleteById(id);
	}

	@Transactional(rollbackOn=Exception.class)
	@Override
	public void save(List<T> entitys) {
		for(T entity:entitys){
			dao.save(entity);
		}
	}

	@Override
	public Iterable<T> findAll(Predicate condition) {
		return dao.findAll(condition);
	}



}

