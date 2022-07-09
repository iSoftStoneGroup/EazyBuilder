package com.eazybuilder.ci.service;

import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.ProjectStatisticJobRecord;
import com.eazybuilder.ci.repository.ProjectStatisticJobRecordDao;

@Service
public class ProjectStatisticJobRecordService
		extends AbstractCommonServiceImpl<ProjectStatisticJobRecordDao, ProjectStatisticJobRecord>
		implements CommonService<ProjectStatisticJobRecord> {

}
