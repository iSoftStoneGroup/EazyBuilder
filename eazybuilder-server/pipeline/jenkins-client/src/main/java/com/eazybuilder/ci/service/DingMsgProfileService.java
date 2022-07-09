package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.DingMsgProfile;
import com.eazybuilder.ci.repository.DingMsgProfileDao;
import org.springframework.stereotype.Service;


@Service
public class DingMsgProfileService extends AbstractCommonServiceImpl<DingMsgProfileDao, DingMsgProfile>
        implements CommonService<DingMsgProfile> {

}
