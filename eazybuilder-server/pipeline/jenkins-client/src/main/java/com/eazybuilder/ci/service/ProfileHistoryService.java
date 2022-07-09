package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.ProfileHistory;
import com.eazybuilder.ci.entity.QProfileHistory;
import com.eazybuilder.ci.repository.ProfileHistoryDao;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProfileHistoryService extends AbstractCommonServiceImpl<ProfileHistoryDao, ProfileHistory>
				implements CommonService<ProfileHistory>{


	public Page<ProfileHistory> pageHistory(Pageable pageable, String profileId) {
		BooleanExpression condition=null;

		if(StringUtils.isNotBlank(profileId)){
			condition= QProfileHistory.profileHistory.profileId.eq(profileId);
		}

		return condition==null?dao.findAll(pageable):dao.findAll(condition, pageable);
	}

}
