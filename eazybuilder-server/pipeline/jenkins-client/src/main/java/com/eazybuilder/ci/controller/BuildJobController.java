package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.util.JsonMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.eazybuilder.ci.OperLog;
import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.base.PageResult;
import com.eazybuilder.ci.controller.vo.UserVo;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.entity.report.Status;
import com.eazybuilder.ci.service.BuildJobService;
import com.eazybuilder.ci.service.PipelineServiceImpl;
import com.eazybuilder.ci.util.AuthUtils;
import com.querydsl.core.types.Predicate;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/api/job")
public class BuildJobController extends CRUDRestController<BuildJobService, BuildJob>{

	private static Logger logger=LoggerFactory.getLogger(BuildJobController.class);
	
	@Autowired
    PipelineServiceImpl pipelineServiceImpl;


	@RequestMapping(value="/jobOnline",method=RequestMethod.GET)
	@ApiOperation("仅包含上线的数据")
	public PageResult<BuildJobVo> pipelineByGitpaths(
			@RequestParam(value="limit",defaultValue="10")int limit,
			@RequestParam(value="offset")int offset,
			@RequestParam(value="search",required=false)String searchText){
		Pageable pageable= PageRequest.of(Math.floorDiv(offset, limit), limit, Sort.Direction.DESC,"id");
		Page<BuildJob> page=service.pageSearchOnline(pageable, searchText);
		List<BuildJobVo> buildJobVos= Lists.newArrayList();
		page.forEach(buildJob -> {
			BuildJobVo buildJobVo = new BuildJobVo();
			BeanUtils.copyProperties(buildJob,buildJobVo);
			buildJobVos.add(buildJobVo);
        	if(buildJob.getStatus()==Status.IN_PROGRESS){
				List<JobOnlineProjectVo> jobOnlineProjectVos = new ArrayList<>();
				List<Pipeline> pipelines = pipelineServiceImpl.findByJobId(buildJob.getId());
				for(Project project:buildJob.getProjects()){
					for(Pipeline pipeline:pipelines){
						if(project.getId().equals(pipeline.getProject().getId())){
							JobOnlineProjectVo jobOnlineProjectVo = new JobOnlineProjectVo();
							jobOnlineProjectVo.setName(project.getName());
							jobOnlineProjectVo.setDescription(project.getDescription());
							jobOnlineProjectVo.setStages(pipeline.getStages());
							jobOnlineProjectVo.setDtpReports(pipeline.getDtpReports());
							jobOnlineProjectVo.setMetrics(pipeline.getMetrics());
							jobOnlineProjectVo.setLogId(pipeline.getLogId());
							jobOnlineProjectVo.setTargetBranch(pipeline.getTargetBranch());
							jobOnlineProjectVo.setSonarKey(pipeline.getProject().getSonarKey());
							jobOnlineProjectVo.setStatus(pipeline.getStatus());
							jobOnlineProjectVo.setStatusGuard(pipeline.getStatusGuard());
							jobOnlineProjectVos.add(jobOnlineProjectVo);
						}
					}
				}
				buildJobVo.setJobOnlineProjectVos(jobOnlineProjectVos);
			}
		});

		return PageResult.create(page.getTotalElements(), buildJobVos);
	}

	/**
	 * 	用Redis等分布式缓存代替(如果是集群方式)
	 */
	Cache<String,Status> jobStatusCache =CacheBuilder.newBuilder()
				.maximumSize(10000)
				.expireAfterWrite(30, TimeUnit.MINUTES).build();
	
	@RequestMapping(method={RequestMethod.POST,RequestMethod.PUT})
	@ApiOperation("保存")
	@Override
	public BuildJob save(@RequestBody BuildJob entity) {
		if(entity.getTriggerType()==JobTrigger.cron) {
			if(!CronSequenceGenerator.isValidExpression(entity.getCron())){
				throw new IllegalArgumentException("错误的CRON表达式");
			}
			Date nextTime=new CronTrigger(entity.getCron()).nextExecutionTime(new SimpleTriggerContext());
			entity.setNextTime(nextTime.getTime());
		}
		return super.save(entity);
	}
	
	@RequestMapping(value="/trigger",method=RequestMethod.POST)
	@ApiOperation("立即触发任务")
	@OperLog(module = "job",opType = "triggerByManual",opDesc = "手工执行任务")
	public void trigger(@RequestBody List<String>  ids){
		final UserVo currentUser=AuthUtils.getCurrentUser();
		new Thread(new Runnable() {
			@Override
			public void run() {
				//user who trigger build job
				AuthUtils.ACCESS_USER.set(currentUser);
				ids.forEach(id->{
					BuildJob job=service.findOne(id);
					jobStatusCache.put(job.getId(), Status.IN_PROGRESS);
					try {
						pipelineServiceImpl.triggerBatchPipeline(job);
						job.setStatus(Status.IN_PROGRESS);
						service.save(job);
					} catch (Exception e) {
						logger.error("",e);
					}finally{
						jobStatusCache.invalidate(job.getId());
					}
				});
			}
		}).start();
	}
	@RequestMapping(value="/status",method=RequestMethod.GET)
	public Status getStatus(@RequestParam("id")String jobId){
		Status status= jobStatusCache.getIfPresent(jobId);
		if(status==null){
			status=Status.NOT_EXECUTED;
		}
		return status;
	}

	@RequestMapping(value="/findByQueryDlsList",method=RequestMethod.GET)
	public Iterable<BuildJob> findByQueryDlsList(@QuerydslPredicate(root=BuildJob.class) Predicate predicate ){
		return service.findAll(predicate);
	}

}
