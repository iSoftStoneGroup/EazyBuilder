package com.eazybuilder.ci.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eazybuilder.ci.entity.Metric;
@Repository
public interface MetricDao extends BaseDao<Metric, String>{

	@Query(nativeQuery=true,value="select m.pipeline_id from ci_project p ,ci_pipeline_history h , ci_metric m where p.id=h.project_id and h.id=m.pipeline_id and p.id=:projectId and m.type like 'bug%' order by m.time desc limit 1")
	public String getSuccessSonarScanPipeLineId(@Param("projectId")String projectId);
	
	@Query(nativeQuery=true,value="select m.pipeline_id from ci_project p ,ci_pipeline_history h , ci_metric m where p.id=h.project_id and h.id=m.pipeline_id and p.id=:projectId and m.type like 'dc%' order by m.time desc limit 1")
	public String getSuccessDCScanPipeLineId(@Param("projectId")String projectId);
}
