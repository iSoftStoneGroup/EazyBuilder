package com.eazybuilder.ci.event.listener;

import com.eazybuilder.ci.constant.PreTaskStatus;
import com.eazybuilder.ci.entity.BuildJob;
import com.eazybuilder.ci.event.EventListener;
import com.eazybuilder.ci.service.BuildJobService;
import com.eazybuilder.ci.service.PipelineServiceImpl;
import com.eazybuilder.ci.service.async.PipelineExecuteResult;
import com.eazybuilder.ci.util.JsonMapper;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@EventListener
public class BuildJobDoneListener {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	PipelineServiceImpl pipelineService;

	@Resource
	Redisson redisson;


	@Autowired
	BuildJobService buildJobService;
	
	@Subscribe
	public void onFinished(List<PipelineExecuteResult> results) throws Exception {
		logger.info("定时构建任务执行完毕，触发监听事件任务！");
		logger.info("当前流水线执行结果信息：{}",JsonMapper.nonDefaultMapper().toJson(results));
		String jobId =  results.get(0).getJobId();
		 List<Boolean>pilelineFlags=results.stream().map(PipelineExecuteResult::getSuccess)
				 .map(success-> StringUtils.equals("SUCCESS",success)|| StringUtils.equals("true",success) ).collect(Collectors.toList());
		 boolean isAllFailed = pilelineFlags.stream().reduce(Boolean::logicalOr).get();
		 if(!isAllFailed){
			 logger.info("前置任务执行全部失败，返回！");
			 return;
		 }
		RMap<String, String> map = redisson.getMap(BuildJob.getArrangementJobRedisKey(jobId));
		if(MapUtils.isEmpty(map)){
			logger.info("自动构建任务运行结束，未发现后续可触发构建任务！");
			return;
		}
		for(String nextJobId:map.keySet() ){
            BuildJob job = buildJobService.findOne(nextJobId);
			if(job.getPreTaskStatus() == PreTaskStatus.SUCCEED_ALL){
				boolean isAllSucceed = pilelineFlags.stream().reduce(Boolean::logicalAnd).get();
				if(!isAllSucceed){
					continue;
				}
			}
            pipelineService.triggerBatchPipeline(job);
        }
	}
}
