package com.eazybuilder.ci.sql.vo;

import java.util.Map;

public class SQLSource {
	//源文件
	private String source;
	//SQL Map
	private Map<String,String> sqlMap;
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Map<String, String> getSqlMap() {
		return sqlMap;
	}
	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}
}
