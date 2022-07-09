package com.eazybuilder.ci.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.WarnRule;
import com.eazybuilder.ci.service.WarnRuleService;

@RestController
@RequestMapping("/api/warnRule")
public class WarnRuleController extends CRUDRestController<WarnRuleService, WarnRule>{

}
