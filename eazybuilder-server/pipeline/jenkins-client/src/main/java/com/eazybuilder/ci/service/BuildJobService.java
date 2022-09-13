package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.constant.RoleEnum;
import com.eazybuilder.ci.controller.vo.UserVo;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.repository.BuildJobDao;
import com.eazybuilder.ci.util.AuthUtils;
import com.eazybuilder.ci.util.JsonMapper;
import com.eazybuilder.ci.util.RedisUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class BuildJobService extends AbstractCommonServiceImpl<BuildJobDao, BuildJob> implements CommonService<BuildJob>{

	@Autowired
	private JdbcTemplate template;
	
	@Autowired
    TeamServiceImpl teamServiceImpl;
	
	@Autowired
	ProjectService projectService;


	@Resource
	Redisson redisson;

	@Autowired
	RedisUtils redisUtils;

	public void updateNextTime(BuildJob job,Long nextTime){
		template.update("update ci_job set next_time = ? , last_trigger = ? where id=? and next_time=?", new Object[]{nextTime,System.currentTimeMillis(),job.getId(),job.getNextTime()});
		job.setNextTime(nextTime);
	}

	public Iterable<BuildJob> findAllCronJob(){
		return dao.findAll(QBuildJob.buildJob.triggerType.eq(JobTrigger.cron));
	}
	public Page<BuildJob> pageSearchOnline(Pageable pageable, String searchText) {

		BooleanExpression condition=null;
		User currentUser=AuthUtils.getCurrentUser();
		if(currentUser!=null
				&& !Role.existRole(currentUser.getRoles(), RoleEnum.admin)){
			List<String> teamIds=Lists.newArrayList();
			teamServiceImpl.findByUser(currentUser).forEach(team->{
				teamIds.add(team.getId());
			});
			condition=QBuildJob.buildJob.teamId.in(teamIds);
			if(Role.existRole(currentUser.getRoles(), RoleEnum.audit)) {
				condition=condition.or(QBuildJob.buildJob.qaJob.eq(true));
			}else {
				condition=condition.and(QBuildJob.buildJob.qaJob.eq(false));
			}
		}

		if(StringUtils.isNotBlank(searchText)){
			if(condition!=null){
				condition=condition.and(QBuildJob.buildJob.name.like("%"+searchText+"%"));
			}else{
				condition=QBuildJob.buildJob.name.like("%"+searchText+"%");
			}
		}
		if(condition!=null){
			condition=condition.and(QBuildJob.buildJob.onLine.eq(true));
		}else{
			condition=QBuildJob.buildJob.onLine.eq(true);
		}
		return condition==null?dao.findAll(pageable):dao.findAll(condition, pageable);
	}
	@Override
	public Page<BuildJob> pageSearch(Pageable pageable, String searchText) {
		
		BooleanExpression condition=null;
		User currentUser=AuthUtils.getCurrentUser();
		if(currentUser!=null
				&& !Role.existRole(currentUser.getRoles(), RoleEnum.admin)){
			List<String> teamIds=Lists.newArrayList();
			teamServiceImpl.findByUser(currentUser).forEach(team->{
				teamIds.add(team.getId());
			});
			condition=QBuildJob.buildJob.teamId.in(teamIds);
			if(Role.existRole(currentUser.getRoles(), RoleEnum.audit)) {
				condition=condition.or(QBuildJob.buildJob.qaJob.eq(true));
			}else {
				condition=condition.and(QBuildJob.buildJob.qaJob.eq(false));
			}
		}
		
		if(StringUtils.isNotBlank(searchText)){
			if(condition!=null){
				condition=condition.and(QBuildJob.buildJob.name.like("%"+searchText+"%"));
			}else{
				condition=QBuildJob.buildJob.name.like("%"+searchText+"%");
			}
		}
		if(condition!=null){
			condition=condition.and(QBuildJob.buildJob.onLine.eq(false));
		}else{
			condition=QBuildJob.buildJob.onLine.eq(false);
		}
		return condition==null?dao.findAll(pageable):dao.findAll(condition, pageable);
	}

	@Override
	public void save(BuildJob entity) {
		if(entity.getId() != null){
			List<BuildJob> watchJobList = Lists.newArrayList();
			searchWatchJobList(entity,watchJobList);
			Optional<BuildJob> repeatCallJob =  watchJobList.stream().filter(job->StringUtils.equals(job.getWatchJobId(),entity.getId())).findFirst();
			if(repeatCallJob.isPresent()){
				throw new IllegalArgumentException(String.format("当前任务在自动构建任务名为：【%s】中被重复调用，无法保存",repeatCallJob.get().getName()));
			}
			BuildJob original =  this.findOne(entity.getId());
			if(original.getTriggerType() == JobTrigger.watch_job_executed && entity.getTriggerType()!= JobTrigger.watch_job_executed){
				updateRedis(original);
			}
		}
		if(entity.getTriggerType() == JobTrigger.watch_job_executed && entity.getWatchJobId()!= null){
			RMap<String, String> map = redisson.getMap(entity.getArrangementJobRedisKey());
			map.put(entity.getId(),entity.getName());
		}
		super.save(entity);

	}

	private void updateRedis(BuildJob original) {
		RMap<String, String> map = redisson.getMap(original.getArrangementJobRedisKey());
		map.remove(original.getId());
	}


	private void searchWatchJobList(BuildJob entity,List<BuildJob> buildJobs) {
		if(entity.getTriggerType() == JobTrigger.watch_job_executed && entity.getWatchJobId()!= null){
			BuildJob preJob  = this.findOne(entity.getWatchJobId());
			if(preJob != null){
				buildJobs.add(preJob);
				searchWatchJobList(preJob,buildJobs);
			}
		}
	}



	public Page<BuildJob> pageSearchWithExcludes(Pageable pageable, String searchText, ArrayList<String> excludeIds, String teamId) {
			BooleanExpression condition = null;
		    QBuildJob qBuildJob = QBuildJob.buildJob;
			if (StringUtils.isNotBlank(searchText)) {
				condition = qBuildJob.name.like("%" + searchText + "%");
			}
			UserVo currentUser = AuthUtils.getCurrentUser();
		    if (currentUser != null && !currentUser.isAuditReader()) {
			   condition = qBuildJob.teamId.eq(teamId);
		    }
			if (excludeIds != null && excludeIds.size() > 0) {
				if (condition != null) {
					condition = condition.and(qBuildJob.id.notIn(excludeIds));
				} else {
					condition = qBuildJob.id.notIn(excludeIds);
				}
			}
			return condition == null ? dao.findAll(pageable) : dao.findAll(condition, pageable);
		}

}
