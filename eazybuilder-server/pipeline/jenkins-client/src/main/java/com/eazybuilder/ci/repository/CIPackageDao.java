package com.eazybuilder.ci.repository;

import org.springframework.stereotype.Repository;

import com.eazybuilder.ci.entity.pipeline.CIPackage;

@Repository
public interface CIPackageDao extends BaseDao<CIPackage, String>{
}
