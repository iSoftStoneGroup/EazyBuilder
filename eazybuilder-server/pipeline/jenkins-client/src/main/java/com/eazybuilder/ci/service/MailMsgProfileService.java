package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.repository.*;
import org.springframework.stereotype.Service;


@Service
public class MailMsgProfileService extends AbstractCommonServiceImpl<MailMsgProfileDao, MailMsgProfile>
        implements CommonService<MailMsgProfile> {

}
