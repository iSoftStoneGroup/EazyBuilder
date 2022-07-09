package com.eazybuilder.ci.repository;

import org.springframework.stereotype.Repository;

import com.eazybuilder.ci.entity.test.Env;

@Repository
public interface EnvDao extends BaseDao<Env, String>{

}
