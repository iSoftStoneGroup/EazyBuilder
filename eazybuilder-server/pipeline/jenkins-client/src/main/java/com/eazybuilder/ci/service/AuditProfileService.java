package com.eazybuilder.ci.service;

import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.AuditProfile;
import com.eazybuilder.ci.repository.AuditProfileDao;

@Service
public class AuditProfileService extends AbstractCommonServiceImpl<AuditProfileDao,AuditProfile> 
    implements CommonService<AuditProfile>{

}
