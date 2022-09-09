package com.eazybuilder.ci.controller.vo;

import java.util.List;

public class WarnTeamReportVo {
	
	private String teamName;
	
	private Long bugBlockerSum=0L;
	
	private Long vulnerBlockerSum=0L;
	
	private Long codeSmellBlocker=0L;
	
	private Long dcHighSum=0L;
	
	private Double unitTestCoverageRate=0d;
	
	private Long projectNum=0L;
	
	private List<ProjectQAReport> projectQAReports;
	
	private List<ProjectQAReport> warnProjectReports;

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public List<ProjectQAReport> getProjectQAReports() {
		return projectQAReports;
	}

	public void setProjectQAReports(List<ProjectQAReport> projectQAReports) {
		this.projectQAReports = projectQAReports;
	}

	public Long getBugBlockerSum() {
		return bugBlockerSum;
	}

	public void setBugBlockerSum(Long bugBlockerSum) {
		this.bugBlockerSum = bugBlockerSum;
	}

	public Long getVulnerBlockerSum() {
		return vulnerBlockerSum;
	}

	public void setVulnerBlockerSum(Long vulnerBlockerSum) {
		this.vulnerBlockerSum = vulnerBlockerSum;
	}

	public Long getCodeSmellBlocker() {
		return codeSmellBlocker;
	}

	public void setCodeSmellBlocker(Long codeSmellBlocker) {
		this.codeSmellBlocker = codeSmellBlocker;
	}

	public Long getDcHighSum() {
		return dcHighSum;
	}

	public void setDcHighSum(Long dcHighSum) {
		this.dcHighSum = dcHighSum;
	}

	public Double getUnitTestCoverageRate() {
		return unitTestCoverageRate;
	}

	public void setUnitTestCoverageRate(Double unitTestCoverageRate) {
		this.unitTestCoverageRate = unitTestCoverageRate;
	}

	public Long getProjectNum() {
		return projectNum;
	}

	public void setProjectNum(Long projectNum) {
		this.projectNum = projectNum;
	}

	public List<ProjectQAReport> getWarnProjectReports() {
		return warnProjectReports;
	}

	public void setWarnProjectReports(List<ProjectQAReport> warnProjectReports) {
		this.warnProjectReports = warnProjectReports;
	}
}
