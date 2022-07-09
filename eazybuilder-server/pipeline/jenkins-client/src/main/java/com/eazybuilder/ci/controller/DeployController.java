package com.eazybuilder.ci.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.Deploy;
import com.eazybuilder.ci.service.DeployService;

@RestController
@RequestMapping("/api/deploy")
public class DeployController extends CRUDRestController<DeployService, Deploy>{

}
