package com.eazybuilder.ci.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.Metric;
import com.eazybuilder.ci.entity.QMetric;
import com.eazybuilder.ci.repository.MetricDao;

@Service
public class MetricService extends AbstractCommonServiceImpl<MetricDao, Metric> implements CommonService<Metric>{

	
	public Iterable<Metric> findByPipelineId(String pipelineId){
		return dao.findAll(QMetric.metric.pipeline.id.eq(pipelineId));
	}
	
	public List<Metric> findLatestScanInfoByProjectId(String projectId){
		List<Metric> metrics=Lists.newArrayList();
		String sonarScanPipelineId=dao.getSuccessSonarScanPipeLineId(projectId);
		String dcPipelineId=dao.getSuccessDCScanPipeLineId(projectId);
		if(sonarScanPipelineId!=null){
			if(sonarScanPipelineId.equals(dcPipelineId)){
				Iterable<Metric> ppMetric=findByPipelineId(sonarScanPipelineId);
				for(Metric m:ppMetric){
					metrics.add(m);
				}
			}else{
				Iterable<Metric> ppMetric=findByPipelineId(sonarScanPipelineId);
				for(Metric m:ppMetric){
					if(m.getType().name().startsWith("bug")||m.getType().name().startsWith("vulner")){
						metrics.add(m);
					}
				}
				
				if(dcPipelineId!=null){
					ppMetric=findByPipelineId(dcPipelineId);
					for(Metric m:ppMetric){
						if(m.getType().name().startsWith("dc")){
							metrics.add(m);
						}
					}
				}
			}
		}
		
		return metrics;
		
	}
}
