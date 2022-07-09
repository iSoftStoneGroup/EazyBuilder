package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.repository.PipelineBuildDao;
import org.springframework.stereotype.Service;

@Service
public class PipelineBuildService extends AbstractCommonServiceImpl<PipelineBuildDao, PipelineBuild> implements CommonService<PipelineBuild>{
    public PipelineBuild findByHistoryId(String historyId){
        return dao.findOne(QPipelineBuild.pipelineBuild.pipelineHistoryId.eq(historyId)).orElse(null);
    }
}
