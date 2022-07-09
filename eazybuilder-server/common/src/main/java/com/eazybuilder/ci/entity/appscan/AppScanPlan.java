package com.eazybuilder.ci.entity.appscan;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.eazybuilder.ci.constant.ExecuteType;

@Entity
@Table(name="CI_APPSCAN_PLAN",uniqueConstraints=@UniqueConstraint(columnNames="NAME"))
public class AppScanPlan {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(10)")
	private String id;
	private String name;
	private String teamId;
	private ExecuteType executeType;
	private Integer timeoutMinute;
	private String projectId;
	private String cron;
	private String targetStartingUrl;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<AppScanScript> scripts;
	
	/**
	 * 上次触发时间
	 */
	private Long lastTrigger;
	
	/**
	 * 下次执行时间
	 */
	private Long nextTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public ExecuteType getExecuteType() {
		return executeType;
	}

	public void setExecuteType(ExecuteType executeType) {
		this.executeType = executeType;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}
	public Long getLastTrigger() {
		return lastTrigger;
	}

	public void setLastTrigger(Long lastTrigger) {
		this.lastTrigger = lastTrigger;
	}

	public Long getNextTime() {
		return nextTime;
	}

	public void setNextTime(Long nextTime) {
		this.nextTime = nextTime;
	}

	public String getTargetStartingUrl() {
		return targetStartingUrl;
	}

	public void setTargetStartingUrl(String targetStartingUrl) {
		this.targetStartingUrl = targetStartingUrl;
	}

	public List<AppScanScript> getScripts() {
		return scripts;
	}

	public void setScripts(List<AppScanScript> scripts) {
		this.scripts = scripts;
	}

	public Integer getTimeoutMinute() {
		return timeoutMinute;
	}

	public void setTimeoutMinute(Integer timeoutMinute) {
		this.timeoutMinute = timeoutMinute;
	}
	

}
