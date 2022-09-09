package com.eazybuilder.ci.openApi;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.constant.MetricType;
import com.eazybuilder.ci.dto.ProjectLastBuildInfo;
import com.eazybuilder.ci.entity.Metric;
import com.eazybuilder.ci.entity.Pipeline;
import com.eazybuilder.ci.entity.report.Status;
import com.eazybuilder.ci.service.PipelineServiceImpl;
import com.eazybuilder.ci.util.BuildStatusSvgHolder;
import com.eazybuilder.ci.util.SonarMetricSvgHolder;

@RestController
@RequestMapping("/public/")
public class PipelinePublicApi {
	 private static org.slf4j.Logger logger = LoggerFactory.getLogger(PipelinePublicApi.class);
	@Autowired
    PipelineServiceImpl service;

	@RequestMapping(method=RequestMethod.GET,value="/buildStatus/svg")
	public void getLastBuildStatusSvg(@RequestParam("projectId")String projectId,HttpServletResponse response) throws IOException {
		//ProjectLastBuildInfo lastBuild=service.getLastBuild(projectId);
		Pipeline pipeline = service.getLastPipelineByProjectId(projectId);
		Status status= (pipeline==null || pipeline.getStatus() == null ?Status.NOT_EXECUTED:pipeline.getStatus());
		String svg=BuildStatusSvgHolder.SVG_NOTRUN;
		switch(status) {
		case SUCCESS:
		case UNSTABLE:
			svg=BuildStatusSvgHolder.SVG_PASSING;
			break;
		case ASSERT_WARNRULE_FAILED:
		case FAILED:
			svg=BuildStatusSvgHolder.SVG_FAILING;
			break;
		case IN_PROGRESS:
		case WAIT_AUTO_TEST_RESULT:
			svg=BuildStatusSvgHolder.SVG_RUNNING;
			break;
		case ABORTED:
			svg=BuildStatusSvgHolder.SVG_ABORTED;
			break;
		default :
			svg=BuildStatusSvgHolder.SVG_NOTRUN;
			break;
		}
		response.setHeader("Content-Type", "image/svg+xml");
		response.getWriter().write(svg);
		response.getWriter().flush();
	}
	
	
	@RequestMapping(method=RequestMethod.GET,value="/sonarmetric/svg")
	public void getLastSonarMetric(@RequestParam("projectId")String projectId,@RequestParam("type")MetricType type,HttpServletResponse response) throws IOException {
		logger.debug("查询sonarqube徽章:{}",type);
		SonarMetricSvgHolder sonarMetricSvgHolder=null;
		ProjectLastBuildInfo projectLastBuildInfo = service.getProjectLastBuildInfoByProjectId(projectId);
		if(null==projectLastBuildInfo) {
			 sonarMetricSvgHolder=new SonarMetricSvgHolder("0","unexectued"); 
		}else {
			 Map<MetricType,Metric> metrics=projectLastBuildInfo.getMetrics();
//		  String val=metrics.get(MetricType.valueOf(type)).getVal();
			 
		  String typeAbbr;
		  if(type.name().split("_").length>2) {
			  typeAbbr= type.name().split("_")[2];
		  }else {
			  typeAbbr= type.name().split("_")[1];
		  }
		  
		  if(null!=metrics.get(type)) {
			  sonarMetricSvgHolder=new SonarMetricSvgHolder(metrics.get(type).getVal(),typeAbbr);	
		  }else {
			  logger.debug("sonarqube徽章数据在流水线结果中不存在:{}",type);
			  sonarMetricSvgHolder=new SonarMetricSvgHolder("0","unexectued"); 
		  }
		 					 
		}
					
		String svg=sonarMetricSvgHolder.getSONAR_BADGES();
		 
		response.setHeader("Content-Type", "image/svg+xml");
		response.getWriter().write(svg);
		response.getWriter().flush();
	}
	
	
}
