package com.eazybuilder.ci.service;

import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.TeamResource;
import com.eazybuilder.ci.repository.TeamResourceDao;

@Service
public class TeamResourceService extends AbstractCommonServiceImpl<TeamResourceDao, TeamResource> 
	implements CommonService<TeamResource>{

}
