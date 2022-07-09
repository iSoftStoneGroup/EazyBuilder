package com.eazybuilder.ci.entity.appscan;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.eazybuilder.ci.entity.report.Status;
@Entity
@Table(name="CI_APPSCAN_HISTORY",uniqueConstraints=@UniqueConstraint(columnNames="UID"))
public class ScanHistory {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(10)")
	private String id;
	
	private String uid;
	
	private String planId;
	//冗余字段，用于历史记录查询或者关联表被修改的情况
	private String planName;
	//测试目标
	private String targetUrl;
	
	private String teamId;
	//冗余字段,用于历史记录查询或者关联表被修改的情况
	private String teamName;
	
	private Date startTime;
	private Date endTime;
	
	private Status executeStatus;
	
	private String reportFileId;
	//结果摘要
	private Integer totalHigh=0;
	private Integer totalMedium=0;
	private Integer totalLow=0;
	private Integer totalInformational=0;
	
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
	public Integer getTotalHigh() {
		return totalHigh;
	}
	public void setTotalHigh(Integer totalHigh) {
		this.totalHigh = totalHigh;
	}
	public Integer getTotalMedium() {
		return totalMedium;
	}
	public void setTotalMedium(Integer totalMedium) {
		this.totalMedium = totalMedium;
	}
	public Integer getTotalLow() {
		return totalLow;
	}
	public void setTotalLow(Integer totalLow) {
		this.totalLow = totalLow;
	}
	public Integer getTotalInformational() {
		return totalInformational;
	}
	public void setTotalInformational(Integer totalInformational) {
		this.totalInformational = totalInformational;
	}
	public String getTargetUrl() {
		return targetUrl;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	
}
