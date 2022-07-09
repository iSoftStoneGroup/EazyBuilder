package com.eazybuilder.ci.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.SysLog;
import com.eazybuilder.ci.service.SysLogService;

@RestController
@RequestMapping("/api/syslog")
public class SysLogController  extends CRUDRestController<SysLogService, SysLog>{

}
