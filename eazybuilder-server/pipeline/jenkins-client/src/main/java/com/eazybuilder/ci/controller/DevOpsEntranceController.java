package com.eazybuilder.ci.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.DevOpsEntrance;
import com.eazybuilder.ci.service.DevOpsEntranceService;

@RestController
@RequestMapping("/api/entrance")
public class DevOpsEntranceController extends CRUDRestController<DevOpsEntranceService, DevOpsEntrance>{

}
