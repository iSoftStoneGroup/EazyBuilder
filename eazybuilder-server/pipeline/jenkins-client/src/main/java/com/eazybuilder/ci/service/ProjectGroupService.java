package com.eazybuilder.ci.service;

import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.ProjectGroup;
import com.eazybuilder.ci.repository.ProjectGroupDao;

@Service
public class ProjectGroupService extends AbstractCommonServiceImpl<ProjectGroupDao, ProjectGroup> 
		implements CommonService<ProjectGroup>{

}
