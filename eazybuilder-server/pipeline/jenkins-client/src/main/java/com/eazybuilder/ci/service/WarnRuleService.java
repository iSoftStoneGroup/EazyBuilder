package com.eazybuilder.ci.service;

import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.WarnRule;
import com.eazybuilder.ci.repository.WarnRuleDao;
@Service
public class WarnRuleService extends AbstractCommonServiceImpl<WarnRuleDao, WarnRule> implements CommonService<WarnRule>{

}
