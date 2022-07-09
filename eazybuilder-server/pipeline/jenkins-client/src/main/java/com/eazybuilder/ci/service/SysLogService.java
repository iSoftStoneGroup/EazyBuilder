package com.eazybuilder.ci.service;

import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.SysLog;
import com.eazybuilder.ci.repository.SysLogDao;
@Service
public class SysLogService extends AbstractCommonServiceImpl<SysLogDao, SysLog> 
	implements CommonService<SysLog>{

}
