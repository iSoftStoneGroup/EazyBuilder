package com.eazybuilder.ci.controller;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import com.eazybuilder.ci.constant.RoleEnum;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.eazybuilder.ci.constant.MetricType;
import com.eazybuilder.ci.entity.QPipeline;
import com.eazybuilder.ci.entity.Role;
import com.eazybuilder.ci.entity.User;
import com.eazybuilder.ci.repository.MetricDao;
import com.eazybuilder.ci.repository.PipelineDao;
import com.eazybuilder.ci.repository.ProjectDao;
import com.eazybuilder.ci.repository.TeamDao;
import com.eazybuilder.ci.repository.UserDao;
import com.eazybuilder.ci.service.ProjectService;
import com.eazybuilder.ci.service.TeamServiceImpl;
import com.eazybuilder.ci.util.AuthUtils;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/chart")
public class DashboardChartController {
	
	private static final String QUERY_SUM_METRIC="select sum(val) from ci_metric m, "+
			" (SELECT max(a.id) as mid,c.name FROM ci_metric a, ci_pipeline_history b, ci_project c "+
			" WHERE a.pipeline_id = b.id AND b.project_id = c.id AND a.type = ? GROUP BY c.name)p where m.id=p.mid";
	
	private static final String QUERY_SUM_METRIC_FILTER_BY_PROJECT="select sum(val) from ci_metric m, "+
			" (SELECT max(a.id) as mid,c.name FROM ci_metric a, ci_pipeline_history b, ci_project c "+
			" WHERE a.pipeline_id = b.id AND b.project_id = c.id AND a.type = ? and c.id in (%s) GROUP BY c.name)p where m.id=p.mid";
	
	private static final String QUERY_PROJECT_METRIC="select sum(val) from ci_metric m, "+
			" (SELECT max(a.id) as mid,c.name FROM ci_metric a, ci_pipeline_history b, ci_project c "+
			" WHERE a.pipeline_id = b.id AND b.project_id = c.id AND a.type = ? AND c.id=? GROUP BY c.name)p where m.id=p.mid";

	private static String  SQL_GET_TOP10_BUG = getSqlByPath("/sql/getTop10Bug.sql");
	private static String  SQL_GET_TOP10_BUG_FILTER_BY_PROJECT = getSqlByPath("/sql/getTop10BugFilterByProject.sql");
	private static String SQL_GET_TOP10_CODESMELL = getSqlByPath("/sql/getTop10CodeSmell.sql");
	private static String  SQL_GET_TOP10_CODESMELL_FILTER_BY_PROJECT = getSqlByPath("/sql/getTop10CodeSmellFilterByProject.sql");
	private static String  SQL_GET_TOP10_VULNER = getSqlByPath("/sql/getTop10Vulner.sql");
	private static String  SQL_GET_TOP10_VULNER_FILTER_BY_PROJECT = getSqlByPath("/sql/getTop10VulnerFilterByProject.sql");
	private static String  SQL_GET_TOP10_DC = getSqlByPath("/sql/getTop10DC.sql");
	private static String SQL_GET_TOP10_DC_FILTER_BY_PROJECT= getSqlByPath("/sql/getTop10DCFilterByProject.sql");
	
	@Autowired
	ProjectDao projectDao;
	
	@Autowired
	ProjectService projectService;

	@Autowired
    TeamServiceImpl teamServiceImpl;
	
	@Autowired
	PipelineDao pipelineDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	TeamDao teamDao;
	
	@Autowired
	MetricDao metricDao;
	
	@Autowired
	JdbcTemplate template;

	public static String getSqlByPath(String path){
		try(InputStream input=
					DashboardChartController.class.getResourceAsStream(path)){
			return IOUtils.toString(input, "utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取指定统计项的值（如存在多个取最后一次）
	 * @param type
	 * @param projectId 空则查询所有项目
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="/metric")
	@ApiOperation("获取指定统计项的值（如存在多个取最后一次）")
	public String getMetric(@RequestParam("type")MetricType type,
			@RequestParam(value="project",required=false)String projectId){
		if(StringUtils.isNotBlank(projectId)){
			return template.queryForObject(QUERY_PROJECT_METRIC,new Object[]{type.name(),projectId},String.class);
		}else{
			return template.queryForObject(QUERY_SUM_METRIC,new Object[]{type.name()},String.class);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/totalProject")
	@ApiOperation("项目数量")
	public Long getTotalProject(){
		User currentUser=AuthUtils.getCurrentUser();
		List<Role> roles = currentUser.getRoles();
		
		if(Role.existRole(roles,RoleEnum.admin) || Role.existRole(roles,RoleEnum.audit)) {
			return projectDao.count();
		}else {
			Collection<String> teamIds= teamServiceImpl.getMyTeamIds();
			if(teamIds==null||teamIds.size()==0) {
				return 0L;
			}
			return new Long(projectService.getMyProjectIds().size());
		}
	}
	@RequestMapping(method=RequestMethod.GET,value="/totalCodeLine")
	@ApiOperation("总代码行数")
	public Long getTotalCodeLine(){
		User currentUser=AuthUtils.getCurrentUser();
		List<Role> roles = currentUser.getRoles();
		if(Role.existRole(roles,RoleEnum.admin) || Role.existRole(roles,RoleEnum.audit)) {
			return template.queryForObject(QUERY_SUM_METRIC,new Object[]{MetricType.code_line.name()},Long.class);
		}else {
			Collection<String> projectIds=projectService.getMyProjectIds();
			if(projectIds.isEmpty()) {
				return 0L;
			}
			return template.queryForObject(String.format(QUERY_SUM_METRIC_FILTER_BY_PROJECT,
					StringUtils.join(projectIds,",")),new Object[]{MetricType.code_line.name()},Long.class);
		}
	}
	@RequestMapping(method=RequestMethod.GET,value="/totalBuild")
	@ApiOperation("总构建次数")
	public Long getTotalBuild(){
		User currentUser=AuthUtils.getCurrentUser();
		List<Role> roles = currentUser.getRoles();
		if(Role.existRole(roles,RoleEnum.admin) || Role.existRole(roles,RoleEnum.audit)) {
			return pipelineDao.count();
		}
		Collection<String> projectIds=projectService.getMyProjectIds();
		if(projectIds.isEmpty()) {
			return 0L;
		}
		return pipelineDao.count(QPipeline.pipeline.project.id.in(projectIds));
	}
	@RequestMapping(method=RequestMethod.GET,value="/totalDevelopers")
	@ApiOperation("开发者数量")
	public Long getTotalDevelopers(){
		User currentUser=AuthUtils.getCurrentUser();
		List<Role> roles = currentUser.getRoles();
		if(Role.existRole(roles,RoleEnum.admin) || Role.existRole(roles,RoleEnum.audit)) {
			return userDao.count();
		}else {
			Collection<String> teamIds= teamServiceImpl.getMyTeamIds();
			if(teamIds==null||teamIds.size()==0) {
				return 0L;
			}
			return template.queryForObject("select   count( distinct members_id) from ci_team_members where team_id in ("+
						StringUtils.join(teamIds,",")+")", Long.class);
		}
	}



	
	@RequestMapping(method=RequestMethod.GET,value="/top10Bug")
	public List getTop10Bug(){
		 if(AuthUtils.isAuditOrAdmin()){
			 return template.queryForList(SQL_GET_TOP10_BUG);
		 }
		Collection<String> projectIds=projectService.getMyProjectIds();
		if(projectIds.isEmpty()) {
			return Lists.newArrayList();
		}
		return template.queryForList(String.format(SQL_GET_TOP10_BUG_FILTER_BY_PROJECT, 
				StringUtils.join(projectIds,",")));
	}




	@RequestMapping(method=RequestMethod.GET,value="/top10Vulner")
	public List getTop10Vulner(){
		if(AuthUtils.isAuditOrAdmin()){
			return template.queryForList(SQL_GET_TOP10_VULNER);
		}
		Collection<String> projectIds=projectService.getMyProjectIds();
		if(projectIds.isEmpty()) {
			return Lists.newArrayList();
		}
		return template.queryForList(String.format(SQL_GET_TOP10_VULNER_FILTER_BY_PROJECT, 
				StringUtils.join(projectIds,",")));
	}



	@RequestMapping(method=RequestMethod.GET,value="/top10CodeSmell")
	public List getTop10CodeSmell(){
		User currentUser=AuthUtils.getCurrentUser();
		List<Role> roles = currentUser.getRoles();
		if(Role.existRole(roles,RoleEnum.admin) || Role.existRole(roles,RoleEnum.audit)) {
			return template.queryForList(SQL_GET_TOP10_CODESMELL);
		}
		Collection<String> projectIds=projectService.getMyProjectIds();
		if(projectIds.isEmpty()) {
			return Lists.newArrayList();
		}
		return template.queryForList(String.format(SQL_GET_TOP10_CODESMELL_FILTER_BY_PROJECT, 
				StringUtils.join(projectIds,",")));
	}
	@RequestMapping(method=RequestMethod.GET,value="/top10DC")
	public List getTop10DCResult(){
		User currentUser=AuthUtils.getCurrentUser();
		List<Role> roles = currentUser.getRoles();
		if(Role.existRole(roles,RoleEnum.admin) || Role.existRole(roles,RoleEnum.audit)) {
			return template.queryForList(SQL_GET_TOP10_DC);
		}
		Collection<String> projectIds=projectService.getMyProjectIds();
		if(projectIds.isEmpty()) {
			return Lists.newArrayList();
		}
		return template.queryForList(String.format(SQL_GET_TOP10_DC_FILTER_BY_PROJECT, 
				StringUtils.join(projectIds,",")));
	}
}
