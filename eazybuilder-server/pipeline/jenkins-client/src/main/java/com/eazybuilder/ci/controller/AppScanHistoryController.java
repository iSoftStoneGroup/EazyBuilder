package com.eazybuilder.ci.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.appscan.ScanDetail;
import com.eazybuilder.ci.entity.appscan.ScanHistory;
import com.eazybuilder.ci.service.ScanDetailService;
import com.eazybuilder.ci.service.ScanHistoryService;

@RestController
@RequestMapping("/api/appScanHistory")
public class AppScanHistoryController extends CRUDRestController<ScanHistoryService, ScanHistory>{

	@Autowired
	ScanDetailService detailService;
	
	@RequestMapping(value="/detail",method=RequestMethod.GET)
	public Iterable<ScanDetail> findByHistoryId(@RequestParam("hisId")String hisId){
		return detailService.findByHistoryId(hisId);
	}
}
