package com.eazybuilder.ci.controller;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.UserActivityStatistic;
import com.eazybuilder.ci.service.UserActivityStatisticService;

@RestController
@RequestMapping("/api/userActivityStatistic")
public class UserActivitysStatisticController extends CRUDRestController<UserActivityStatisticService, UserActivityStatistic>{

	@Autowired
	private JdbcTemplate template;
	
	private static String SQL_GROUPBY_USER;
	private static String SQL_GROUPBY_PROJECT;
	private static String SQL_GROUPBY_PROJECT_USER;
	
	static{
		try(InputStream input=
				DashboardChartController.class.getResourceAsStream("/sql/activiti_groupby_user.sql")){
			SQL_GROUPBY_USER=IOUtils.toString(input, "utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try(InputStream input=
				DashboardChartController.class.getResourceAsStream("/sql/activiti_groupby_project.sql")){
			SQL_GROUPBY_PROJECT=IOUtils.toString(input, "utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try(InputStream input=
				DashboardChartController.class.getResourceAsStream("/sql/activiti_groupby_projectAndUser.sql")){
			SQL_GROUPBY_PROJECT_USER=IOUtils.toString(input, "utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@GetMapping("/groupByUser")
	public List getUserActivities(@RequestParam("start")String start,@RequestParam("end")String end){
		return template.queryForList(SQL_GROUPBY_USER, start,end);
	}
	
	@GetMapping("/groupByProject")
	public List getProjectActivities(@RequestParam("start")String start,@RequestParam("end")String end){
		return template.queryForList(SQL_GROUPBY_PROJECT, start,end);
	}
	
	@GetMapping("/groupByProjectUser")
	public List getProjectUserActivities(@RequestParam("start")String start,@RequestParam("end")String end){
		return template.queryForList(SQL_GROUPBY_PROJECT_USER, start,end);
	}
}
