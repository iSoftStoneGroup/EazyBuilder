package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.PipelineLog;
import com.eazybuilder.ci.repository.PipeLineLogDao;
import org.springframework.stereotype.Service;

@Service
public class PipelineLogService extends AbstractCommonServiceImpl<PipeLineLogDao, PipelineLog> implements CommonService<PipelineLog>{

}
