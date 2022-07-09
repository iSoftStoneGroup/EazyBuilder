package com.eazybuilder.ci.dto;

import java.util.Date;
import java.util.LinkedHashMap;

import com.google.common.collect.Maps;
import com.eazybuilder.ci.entity.report.Status;

public class AutomaticTestResult {

	private String executeUid;
	private String testPlanId;
	private Date startExecuteTime;
	private Date endExecuteTime;
	private Status status;
	private Integer totalPass=0;
	private Integer totalFailed=0;
	private Integer totalWarning=0;
	private LinkedHashMap<String,Summary> scriptsSummary=Maps.newLinkedHashMap();
	private String reportFileId;
	private String remindMsg;
	
	public static class Summary{
		private Integer totalPass=0;
		private Integer totalFailed=0;
		private Integer totalWarning=0;
		
		public Summary() {
		}
		
		
		public Summary(Integer totalPass, Integer totalFailed, Integer totalWarning) {
			this.totalPass = totalPass;
			this.totalFailed = totalFailed;
			this.totalWarning = totalWarning;
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
	}
	
	public String getExecuteUid() {
		return executeUid;
	}
	public void setExecuteUid(String executeUid) {
		this.executeUid = executeUid;
	}
	public String getTestPlanId() {
		return testPlanId;
	}
	public void setTestPlanId(String testPlanId) {
		this.testPlanId = testPlanId;
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
	public LinkedHashMap<String,Summary> getScriptsSummary() {
		return scriptsSummary;
	}
	public void setScriptsSummary(LinkedHashMap<String,Summary> scriptsSummary) {
		this.scriptsSummary = scriptsSummary;
	}
}
