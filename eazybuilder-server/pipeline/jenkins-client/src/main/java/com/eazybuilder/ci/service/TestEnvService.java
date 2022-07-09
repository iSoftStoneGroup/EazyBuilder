package com.eazybuilder.ci.service;

import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.test.Env;
import com.eazybuilder.ci.repository.EnvDao;

@Service
public class TestEnvService extends AbstractCommonServiceImpl<EnvDao, Env> 
		implements CommonService<Env>{

}
