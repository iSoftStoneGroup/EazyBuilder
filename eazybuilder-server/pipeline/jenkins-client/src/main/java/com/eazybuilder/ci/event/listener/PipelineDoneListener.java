package com.eazybuilder.ci.event.listener;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.Subscribe;
import com.eazybuilder.ci.entity.Pipeline;
import com.eazybuilder.ci.entity.report.Stage;
import com.eazybuilder.ci.entity.report.Status;
import com.eazybuilder.ci.entity.test.IntegrateTestPlan;
import com.eazybuilder.ci.event.EventListener;
import com.eazybuilder.ci.service.IntegrateTestPlanService;

@EventListener
public class PipelineDoneListener {

	private static Logger logger=LoggerFactory.getLogger(PipelineDoneListener.class);
	
	@Autowired
	IntegrateTestPlanService itService;
	
	@Subscribe
	public void onFinished(Pipeline pipeline){
		if(pipeline==null){
			return;
		}
		logger.info("pipeline finished with status:"+pipeline.getStatus());
		if(pipeline.getStatus()==Status.SUCCESS||pipeline.getStatus()==Status.UNSTABLE){
			
			if(pipeline.getStages()!=null){
				for(Stage stage:pipeline.getStages()){
					//成功部署后，触发对应的测试计划
					if(stage.getName().contains("deploy")){
						Iterable<IntegrateTestPlan> testPlans=itService.findAllTriggerByProjectId(
								pipeline.getProject().getId());
						testPlans.forEach(plan->{
							itService.trigger(plan.getId(), UUID.randomUUID().toString());
						});
						return;
					}
				}
			}
		}
	}
}
