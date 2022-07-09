package com.eazybuilder.ci.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.TeamResource;
import com.eazybuilder.ci.service.TeamResourceService;

@RestController
@RequestMapping("/api/team-resource")
public class TeamResourceController extends CRUDRestController<TeamResourceService, TeamResource>{

}
