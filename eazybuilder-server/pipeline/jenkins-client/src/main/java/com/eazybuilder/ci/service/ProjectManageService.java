package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.ProjectManage;
import com.eazybuilder.ci.repository.ProjectManageDao;
import org.springframework.stereotype.Service;

@Service
public class ProjectManageService extends AbstractCommonServiceImpl<ProjectManageDao, ProjectManage> implements CommonService<ProjectManage>{

}
