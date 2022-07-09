package com.eazybuilder.ci.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.ApiSecurity;
import com.eazybuilder.ci.service.ApiSecurityService;

@RestController
@RequestMapping("/api/apiSecurity")
public class ApiSecurityController extends CRUDRestController<ApiSecurityService, ApiSecurity>{

	
	@PostMapping("/gen-token")
	public Map<String,String> generateToken(@RequestBody ApiSecurity vo) throws Exception {
		Map<String,String> data=Maps.newHashMap();
		data.put("token", service.generateApiToken(vo));
		return data;
	}
}
