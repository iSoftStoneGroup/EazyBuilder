package com.eazybuilder.ci.repository;

import org.springframework.stereotype.Repository;

import com.eazybuilder.ci.entity.Deploy;

@Repository
public interface DeployDao extends BaseDao<Deploy, String>{

}
