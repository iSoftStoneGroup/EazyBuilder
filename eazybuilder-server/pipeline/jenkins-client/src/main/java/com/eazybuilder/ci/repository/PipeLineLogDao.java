package com.eazybuilder.ci.repository;

import com.eazybuilder.ci.entity.PipelineLog;
import org.springframework.stereotype.Repository;

@Repository
public interface PipeLineLogDao extends BaseDao<PipelineLog, String>{

}
