package com.eazybuilder.ci.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.ScmStatisticJob;
import com.eazybuilder.ci.job.DevActivitiyAnalysisJob;
import com.eazybuilder.ci.service.ScmStatisticJobService;

import cn.hutool.core.date.DateUtil;

@RestController
@RequestMapping("/api/scmStatisticJob")
public class ScmStatisticJobController extends CRUDRestController<ScmStatisticJobService, ScmStatisticJob>{

	private static Logger logger=LoggerFactory.getLogger(ScmStatisticJobController.class);
	
	@Autowired
	DevActivitiyAnalysisJob analyer;
	
	
	@GetMapping("/execute")
	public void executeJob(@RequestParam("job")String jobId) {
		ScmStatisticJob job=service.findOne(jobId);
		analyer.executeJob(job);
	}
	
	@GetMapping("/collect")
	public void collectDuring(@RequestParam("job")String jobId,@RequestParam("start")String startDay,@RequestParam("end")String endDay) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ScmStatisticJob job=service.findOne(jobId);
				logger.info("collect statistic of job {} from {} to {}",jobId,startDay,endDay);
				Date start=DateUtil.parse(startDay,"yyyy-MM-dd");
				Date end=DateUtil.parse(endDay,"yyyy-MM-dd");
				
				for(Date day=start;day.before(end);day=new Date(day.getTime()+24*60*60*1000)) {
					analyer.collect(job, day);
				}
			}
		}).start();
	}
}
