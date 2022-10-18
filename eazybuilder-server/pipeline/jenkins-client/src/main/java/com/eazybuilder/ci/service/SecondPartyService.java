package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.SecondParty;
import com.eazybuilder.ci.repository.SecondPartyDao;
import org.springframework.stereotype.Service;

@Service
public class SecondPartyService extends AbstractCommonServiceImpl<SecondPartyDao, SecondParty> implements CommonService<SecondParty>{

}
