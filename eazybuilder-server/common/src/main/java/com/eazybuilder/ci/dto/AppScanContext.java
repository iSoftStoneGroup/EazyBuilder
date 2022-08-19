package com.eazybuilder.ci.dto;

import java.util.List;

import com.eazybuilder.ci.entity.appscan.AppScanScript;

/**
 * 自动化测试的DTO，Jenkins通过调用QTP/Selenium Agent进行测试时传输
 *
 *
 */
public class AppScanContext {
	private List<AppScanScript> scripts;
	private String targetUrl;
	private Integer timeoutMinute;
	private String uuid;
	private String planId;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public List<AppScanScript> getScripts() {
		return scripts;
	}
	public void setScripts(List<AppScanScript> scripts) {
		this.scripts = scripts;
	}
	public String getTargetUrl() {
		return targetUrl;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	public Integer getTimeoutMinute() {
		return timeoutMinute;
	}
	public void setTimeoutMinute(Integer timeoutMinute) {
		this.timeoutMinute = timeoutMinute;
	}
}
