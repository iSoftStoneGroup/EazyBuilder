package com.eazybuilder.ci.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.appscan.AppScanPlan;
import com.eazybuilder.ci.service.AppScanPlanService;

@RestController
@RequestMapping("/api/appscan/plan")
public class AppScanPlanController extends CRUDRestController<AppScanPlanService, AppScanPlan>{

}
