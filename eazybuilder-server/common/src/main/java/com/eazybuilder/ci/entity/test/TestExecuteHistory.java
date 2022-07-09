package com.eazybuilder.ci.entity.test;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.eazybuilder.ci.entity.report.Status;
@Entity
@Table(name="CI_TEST_HISTORY",uniqueConstraints=@UniqueConstraint(columnNames="UID"))
public class TestExecuteHistory {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(10)")
	private String id;
	
	private String uid;
	
	private String planId;
	//冗余字段，用于历史记录查询或者关联表被修改的情况
	private String planName;
	
	private String teamId;
	//冗余字段,用于历史记录查询或者关联表被修改的情况
	private String teamName;
	
	private Date startTime;
	private Date endTime;
	
	private Status executeStatus;
	
	private String reportFileId;
	//结果摘要
	private Integer totalPass;
	private Integer totalFailed;
	private Integer totalWarning;
	
	private String remindMsg;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Status getExecuteStatus() {
		return executeStatus;
	}
	public void setExecuteStatus(Status executeStatus) {
		this.executeStatus = executeStatus;
	}
	public String getReportFileId() {
		return reportFileId;
	}
	public void setReportFileId(String reportFileId) {
		this.reportFileId = reportFileId;
	}
	public Integer getTotalPass() {
		return totalPass;
	}
	public void setTotalPass(Integer totalPass) {
		this.totalPass = totalPass;
	}
	public Integer getTotalFailed() {
		return totalFailed;
	}
	public void setTotalFailed(Integer totalFailed) {
		this.totalFailed = totalFailed;
	}
	public Integer getTotalWarning() {
		return totalWarning;
	}
	public void setTotalWarning(Integer totalWarning) {
		this.totalWarning = totalWarning;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getRemindMsg() {
		return remindMsg;
	}
	public void setRemindMsg(String remindMsg) {
		this.remindMsg = remindMsg;
	}
	
}
