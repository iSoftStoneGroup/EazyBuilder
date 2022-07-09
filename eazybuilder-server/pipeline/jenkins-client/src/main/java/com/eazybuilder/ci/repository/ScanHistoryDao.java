package com.eazybuilder.ci.repository;

import org.springframework.stereotype.Repository;

import com.eazybuilder.ci.entity.appscan.ScanHistory;

@Repository
public interface ScanHistoryDao extends BaseDao<ScanHistory,String>{

}
