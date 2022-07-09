package com.eazybuilder.ci.dto;

import java.util.List;

import com.eazybuilder.ci.entity.report.Status;

public class IssueSummary implements Comparable<IssueSummary>{
	private Status status=Status.SUCCESS;
	private String remingMsg;
	/**
	 * 级别
	 */
	private Severity severity;
	/**
	 * 数量
	 */
	private Integer count;
	/**
	 * 漏洞名称
	 */
	private String name;
	/**
	 * 分类
	 */
	private String type;
	/**
	 * 原因
	 */
	private String cause;
	/**
	 * 风险
	 */
	private String risk;
	/**
	 * 涉及的URL
	 */
	private List<String> urls;
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getRisk() {
		return risk;
	}
	public void setRisk(String risk) {
		this.risk = risk;
	}
	public List<String> getUrls() {
		return urls;
	}
	public void setUrls(List<String> urls) {
		this.urls = urls;
	}
	@Override
	public String toString() {
		return "IssueSummary [severity=" + severity + ", count=" + count + ", name=" + name + ", type=" + type
				+ ", cause=" + cause + ", risk=" + risk + ", urls=" + urls + "]";
	}
	@Override
	public int compareTo(IssueSummary o) {
		if(this.severity.compareTo(o.getSeverity())==0){
			return o.getCount().compareTo(this.getCount());
		}else{
			return this.severity.compareTo(o.getSeverity());
		}
	}
	public Severity getSeverity() {
		return severity;
	}
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getRemingMsg() {
		return remingMsg;
	}
	public void setRemingMsg(String remingMsg) {
		this.remingMsg = remingMsg;
	}

}
