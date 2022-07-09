package com.eazybuilder.ci.service;

import java.util.List;

import com.eazybuilder.ci.constant.RoleEnum;
import com.eazybuilder.ci.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.repository.BuildJobDao;
import com.eazybuilder.ci.util.AuthUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
@Service
public class BuildJobService extends AbstractCommonServiceImpl<BuildJobDao, BuildJob> implements CommonService<BuildJob>{

	@Autowired
	private JdbcTemplate template;
	
	@Autowired
    TeamServiceImpl teamServiceImpl;
	
	@Autowired
	ProjectService projectService;
	
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
	
	
}
