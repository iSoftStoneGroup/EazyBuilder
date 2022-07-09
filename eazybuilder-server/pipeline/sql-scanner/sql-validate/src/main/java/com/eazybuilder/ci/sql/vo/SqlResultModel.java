package com.eazybuilder.ci.sql.vo;

public class SqlResultModel extends SQLValidateResult{
	private String sqlId;
	private String sql;
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getSqlId() {
		return sqlId;
	}
	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
	}
}
