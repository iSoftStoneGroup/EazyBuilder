package com.eazybuilder.ci.repository;

import org.springframework.stereotype.Repository;

import com.eazybuilder.ci.entity.AuditProfile;

@Repository
public interface AuditProfileDao extends BaseDao<AuditProfile, String>{

}
