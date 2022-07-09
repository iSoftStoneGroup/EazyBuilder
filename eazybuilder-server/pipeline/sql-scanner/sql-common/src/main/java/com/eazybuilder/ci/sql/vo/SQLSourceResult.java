package com.eazybuilder.ci.sql.vo;

import java.util.Map;

public class SQLSourceResult extends SQLSource{

	private Map<String,SQLValidateResult> resultMap;

	public SQLSourceResult() {
	}
	public SQLSourceResult(SQLSource source) {
		this.setSource(source.getSource());
		this.setSqlMap(source.getSqlMap());
	}
	
	public Map<String,SQLValidateResult> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String,SQLValidateResult> resultMap) {
		this.resultMap = resultMap;
	}
}
