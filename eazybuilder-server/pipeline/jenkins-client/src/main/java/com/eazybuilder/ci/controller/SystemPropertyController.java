package com.eazybuilder.ci.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.SystemProperty;
import com.eazybuilder.ci.service.SystemPropertyService;

@RestController
@RequestMapping("/api/systemProperty")
public class SystemPropertyController extends CRUDRestController<SystemPropertyService, SystemProperty>{

}
