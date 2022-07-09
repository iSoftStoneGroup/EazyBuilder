package com.eazybuilder.ci.service;

import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.appscan.QScanHistory;
import com.eazybuilder.ci.entity.appscan.ScanHistory;
import com.eazybuilder.ci.repository.ScanHistoryDao;

@Service
public class ScanHistoryService extends AbstractCommonServiceImpl<ScanHistoryDao, ScanHistory> implements CommonService<ScanHistory>{

	public ScanHistory findByUid(String uid){
		return dao.findOne(QScanHistory.scanHistory.uid.eq(uid)).get();
	}
}
