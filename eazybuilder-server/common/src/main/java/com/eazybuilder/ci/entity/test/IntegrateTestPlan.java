package com.eazybuilder.ci.entity.test;

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
@Table(name="CI_TEST_PLAN",uniqueConstraints=@UniqueConstraint(columnNames="NAME"))
public class IntegrateTestPlan {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(10)")
	private String id;
	private String name;
	private String teamId;
	private ExecuteType executeType;
	private String projectId;
	private String cron;
	private String envId;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<AutomaticScript> scripts;
	
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

	public List<AutomaticScript> getScripts() {
		return scripts;
	}

	public void setScripts(List<AutomaticScript> scripts) {
		this.scripts = scripts;
	}

	public String getEnvId() {
		return envId;
	}

	public void setEnvId(String envId) {
		this.envId = envId;
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
	

}
