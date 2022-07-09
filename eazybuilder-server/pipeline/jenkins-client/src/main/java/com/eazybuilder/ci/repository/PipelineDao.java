package com.eazybuilder.ci.repository;

import org.springframework.stereotype.Repository;

import com.eazybuilder.ci.entity.Pipeline;
@Repository
public interface PipelineDao extends BaseDao<Pipeline,String>{

}
