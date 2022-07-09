package com.eazybuilder.ci.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.test.Env;
import com.eazybuilder.ci.service.TestEnvService;

@RestController
@RequestMapping("/api/testEnv")
public class TestEnvController extends CRUDRestController<TestEnvService, Env>{

}
