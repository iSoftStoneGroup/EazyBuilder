package com.eazybuilder.ci.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.test.TestExecuteHistory;
import com.eazybuilder.ci.service.TestExecuteHistoryService;

@RestController
@RequestMapping("/api/testExecHistory")
public class TestExecuteHistoryController extends CRUDRestController<TestExecuteHistoryService, TestExecuteHistory>{

}
