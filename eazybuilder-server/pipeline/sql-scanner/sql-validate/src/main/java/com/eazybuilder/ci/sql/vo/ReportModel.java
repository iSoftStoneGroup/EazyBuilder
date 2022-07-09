package com.eazybuilder.ci.sql.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReportModel {
	private String projectName;
	private int totalSql;
	private int totalFound;
	//sourceFile -> sqlCount
//	private Map<String,SqlCount> summary=new TreeMap<>();//sort by key
	
	private List<SqlCount> summary=new ArrayList<>();
	
	//sourceFile -> Details
	private Map<String,List<SqlResultModel>> detail=new TreeMap<>();
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public int getTotalSql() {
		return totalSql;
	}
	public void setTotalSql(int totalSql) {
		this.totalSql = totalSql;
	}
	public int getTotalFound() {
		return totalFound;
	}
	public void setTotalFound(int totalFound) {
		this.totalFound = totalFound;
	}
	public Map<String, List<SqlResultModel>> getDetail() {
		return detail;
	}
	public void setDetail(Map<String, List<SqlResultModel>> detail) {
		this.detail = detail;
	}
	public List<SqlCount> getSummary() {
		return summary;
	}
	public void setSummary(List<SqlCount> summary) {
		this.summary = summary;
	}
}
