package com.eazybuilder.ci.service;

import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.appscan.QScanDetail;
import com.eazybuilder.ci.entity.appscan.ScanDetail;
import com.eazybuilder.ci.repository.ScanDetailDao;

@Service
public class ScanDetailService extends AbstractCommonServiceImpl<ScanDetailDao, ScanDetail> implements CommonService<ScanDetail>{

	
	public Iterable<ScanDetail> findByHistoryId(String hisId){
		return dao.findAll(QScanDetail.scanDetail.historyId.eq(hisId));
	}
}
