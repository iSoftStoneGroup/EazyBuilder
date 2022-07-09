package com.eazybuilder.ci.repository;

import org.springframework.stereotype.Repository;

import com.eazybuilder.ci.entity.DockerRegistry;

@Repository
public interface DockerRegistryDao extends BaseDao<DockerRegistry, String>{

}
