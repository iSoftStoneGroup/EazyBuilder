package com.eazybuilder.ci.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.ProjectGroup;
import com.eazybuilder.ci.service.ProjectGroupService;

@RestController
@RequestMapping("/api/project-group")
public class ProjectGroupController extends CRUDRestController<ProjectGroupService, ProjectGroup>{

}
