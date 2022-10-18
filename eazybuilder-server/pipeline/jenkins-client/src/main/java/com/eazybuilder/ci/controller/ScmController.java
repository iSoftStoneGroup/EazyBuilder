package com.eazybuilder.ci.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.Scm;
import com.eazybuilder.ci.service.ScmService;

@RestController
@RequestMapping("/api/scm")
public class ScmController extends CRUDRestController<ScmService, Scm>{

}
