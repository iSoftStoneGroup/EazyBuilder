package com.eazybuilder.ci.service;

import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.ScmStatisticJobRecord;
import com.eazybuilder.ci.repository.ScmStatisticJobRecordDao;

@Service
public class ScmStatisticJobRecordService
		extends AbstractCommonServiceImpl<ScmStatisticJobRecordDao, ScmStatisticJobRecord>
		implements CommonService<ScmStatisticJobRecord> {

}
