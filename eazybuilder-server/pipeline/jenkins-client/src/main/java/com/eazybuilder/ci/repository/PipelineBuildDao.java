package com.eazybuilder.ci.repository;

import com.eazybuilder.ci.entity.Guard;
import com.eazybuilder.ci.entity.PipelineBuild;
import org.springframework.stereotype.Repository;

@Repository
public interface PipelineBuildDao extends BaseDao<PipelineBuild, String>{

}
