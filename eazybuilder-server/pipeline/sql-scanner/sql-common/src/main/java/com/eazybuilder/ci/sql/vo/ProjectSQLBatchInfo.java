package com.eazybuilder.ci.sql.vo;

import java.util.List;
/**
 * 工程对应的SQL信息
 *
 *
 */
public class ProjectSQLBatchInfo {

	private String projectName;
	
	private List<SQLSource> sqlSources;
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public List<SQLSource> getSqlSources() {
		return sqlSources;
	}
	public void setSqlSources(List<SQLSource> sqlSources) {
		this.sqlSources = sqlSources;
	}
}
