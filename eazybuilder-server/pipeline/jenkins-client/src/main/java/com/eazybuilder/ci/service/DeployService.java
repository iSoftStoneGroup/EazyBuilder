package com.eazybuilder.ci.service;

import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.Deploy;
import com.eazybuilder.ci.repository.DeployDao;
@Service
public class DeployService extends AbstractCommonServiceImpl<DeployDao, Deploy> 
				implements CommonService<Deploy>{

}
