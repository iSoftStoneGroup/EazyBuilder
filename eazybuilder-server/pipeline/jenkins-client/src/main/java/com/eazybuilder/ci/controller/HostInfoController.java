package com.eazybuilder.ci.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.HostInfo;
import com.eazybuilder.ci.service.HostInfoService;

@RestController
@RequestMapping("/api/hostInfo")
public class HostInfoController extends CRUDRestController<HostInfoService, HostInfo>{

}
