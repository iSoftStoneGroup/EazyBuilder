package com.eazybuilder.ci.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.test.IntegrateTestPlan;
import com.eazybuilder.ci.service.IntegrateTestPlanService;

@RestController
@RequestMapping("/api/itPlan")
public class IntegrateTestPlanController extends CRUDRestController<IntegrateTestPlanService, IntegrateTestPlan>{
	
}
