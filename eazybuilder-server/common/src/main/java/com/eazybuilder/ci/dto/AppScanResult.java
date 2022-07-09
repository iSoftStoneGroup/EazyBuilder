package com.eazybuilder.ci.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.eazybuilder.ci.entity.report.Status;

public class AppScanResult {

	private String executeUid;
	private String scanPlanId;
	private String targetUrl;
	
	private Date startExecuteTime;
	private Date endExecuteTime;
	private Map<String,List<IssueSummary>> summarys;
	private String reportFileId;
	
	private Status status;
	private String remindMsg;
	
	public String getExecuteUid() {
		return executeUid;
	}
	public void setExecuteUid(String executeUid) {
		this.executeUid = executeUid;
	}
	public String getScanPlanId() {
		return scanPlanId;
	}
	public void setScanPlanId(String scanPlanId) {
		this.scanPlanId = scanPlanId;
	}
	public String getTargetUrl() {
		return targetUrl;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	public Date getStartExecuteTime() {
		return startExecuteTime;
	}
	public void setStartExecuteTime(Date startExecuteTime) {
		this.startExecuteTime = startExecuteTime;
	}
	public Date getEndExecuteTime() {
		return endExecuteTime;
	}
	public void setEndExecuteTime(Date endExecuteTime) {
		this.endExecuteTime = endExecuteTime;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Map<String,List<IssueSummary>> getSummarys() {
		return summarys;
	}
	public void setSummarys(Map<String,List<IssueSummary>> summarys) {
		this.summarys = summarys;
	}
	public String getReportFileId() {
		return reportFileId;
	}
	public void setReportFileId(String reportFileId) {
		this.reportFileId = reportFileId;
	}
	public String getRemindMsg() {
		return remindMsg;
	}
	public void setRemindMsg(String remindMsg) {
		this.remindMsg = remindMsg;
	}
}
