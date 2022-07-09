package com.eazybuilder.ci.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.entity.test.TestDetailResult;
import com.eazybuilder.ci.service.TestDetailResultService;

@RestController
@RequestMapping("/api/testDetailResult")
public class TestDetailResultController {

	@Autowired
	TestDetailResultService service;
	
	@RequestMapping(method=RequestMethod.GET)
	public Iterable<TestDetailResult> findByHistoryId(@RequestParam("historyId")String historyId){
		return service.findByHistoryId(historyId);
	}
}
