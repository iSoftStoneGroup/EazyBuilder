package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.repository.ProjectHistoryDao;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProjectHistoryService extends AbstractCommonServiceImpl<ProjectHistoryDao, ProjectHistory>
				implements CommonService<ProjectHistory>{


	public Page<ProjectHistory> pageHistory(Pageable pageable, String projectId) {
		BooleanExpression condition=null;

		if(StringUtils.isNotBlank(projectId)){
			condition=QProjectHistory.projectHistory.projectId.eq(projectId);
		}

		return condition==null?dao.findAll(pageable):dao.findAll(condition, pageable);
	}
}
