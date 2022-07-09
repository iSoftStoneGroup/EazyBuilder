package com.eazybuilder.ci.service;

import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.UserActivityStatistic;
import com.eazybuilder.ci.repository.UserActivityStatisticDao;

@Service
public class UserActivityStatisticService
		extends AbstractCommonServiceImpl<UserActivityStatisticDao, UserActivityStatistic>
		implements CommonService<UserActivityStatistic> {

	
}
