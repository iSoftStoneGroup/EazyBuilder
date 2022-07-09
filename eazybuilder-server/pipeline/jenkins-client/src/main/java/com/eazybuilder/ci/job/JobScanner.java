package com.eazybuilder.ci.job;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import com.eazybuilder.ci.entity.ExecuteType;
import com.eazybuilder.ci.entity.PipelineLog;
import com.eazybuilder.ci.entity.report.Status;
import com.eazybuilder.ci.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Component;

@Component
public class JobScanner implements Runnable{
	private static Logger logger=LoggerFactory.getLogger(JobScanner.class);

	@Autowired
	BuildJobService jobService;
	
	@Autowired
	IntegrateTestPlanService itService;
	
	
	ScheduledExecutorService scheduleService;
	
	@Autowired
    PipelineServiceImpl pipeLineServiceImpl;
	
	@Autowired
	WarnService warnService;
	
	@Autowired
	ReportJob  reportJob;

	@Resource
	PipelineLogService pipelineLogService;
	
	@PostConstruct
	public void init(){
		scheduleService=Executors.newScheduledThreadPool(1);
		scheduleService.scheduleAtFixedRate(this, 30, 60, TimeUnit.SECONDS);
	}

	@Override
	public void run() {
		scanBuildJob();
		
		scanTestJob();
		
		scanReportJob();
	}

	private void scanReportJob() {
		warnService.findAllEnableWarn().forEach(job->{
			try {
				String cron=job.getCron();
				if(System.currentTimeMillis()>job.getNextTime()){
					Date nextTime=new CronTrigger(cron).nextExecutionTime(new SimpleTriggerContext());
					job.setNextTime(nextTime.getTime());
					warnService.save(job);
					
					//run
					reportJob.check(job);
				}
			} catch (Exception e) {
				logger.error("run job error:",e);
			}
		});
	}

	private void scanTestJob() {
		itService.findAllJob().forEach(job->{
			try {
				String cron=job.getCron();
				if(System.currentTimeMillis()>job.getNextTime()){
					//update next time
					Date nextTime=new CronTrigger(cron).nextExecutionTime(new SimpleTriggerContext());
					itService.updateNextTime(job, nextTime.getTime());
					//trigger job
					itService.trigger(job.getId(), UUID.randomUUID().toString());
				}
				
			} catch (Exception e) {
				logger.error("run job error:",e);
			}
		});
	}

	private void scanBuildJob() {
		jobService.findAllCronJob().forEach(job->{
			PipelineLog pipelineLog = new PipelineLog();
			try {
				String cron=job.getCron();
				if(System.currentTimeMillis()>job.getNextTime()){
					pipelineLog.setExecuteType(ExecuteType.timetaskTrigger);
					//update next time
					Date nextTime=new CronTrigger(cron).nextExecutionTime(new SimpleTriggerContext());
					jobService.updateNextTime(job, nextTime.getTime());
					//trigger job
					pipeLineServiceImpl.triggerBatchPipeline(job);
				}
				
			} catch (Exception e) {
				pipelineLog.setName("定时任务名称："+job.getName());
				pipelineLog.setStatus(Status.FAILED);
				pipelineLog.setExceptionLog(e.getMessage());
				logger.error("run job error:",e);
			}finally {
				pipelineLogService.save(pipelineLog);
			}
		});
	}
	
	@PreDestroy
	public void close(){
		try {
			scheduleService.shutdown();
		} catch (Exception e) {
		}
	}
}
