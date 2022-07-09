package com.eazybuilder.ci.service;

import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.ScmStatisticJob;
import com.eazybuilder.ci.repository.ScmStatisticJobDao;

@Service
public class ScmStatisticJobService extends AbstractCommonServiceImpl<ScmStatisticJobDao, ScmStatisticJob> 
		implements CommonService<ScmStatisticJob>{

}
