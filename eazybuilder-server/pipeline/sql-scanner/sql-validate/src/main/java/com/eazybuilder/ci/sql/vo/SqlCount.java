package com.eazybuilder.ci.sql.vo;

public class SqlCount implements Comparable<SqlCount>{
	private String source;
	private int total;
	private int imcompatible;
	
	public int getImcompatible() {
		return imcompatible;
	}
	public void setImcompatible(int imcompatible) {
		this.imcompatible = imcompatible;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	@Override
	public int compareTo(SqlCount o) {
		return new Integer(o.getImcompatible()).compareTo(this.imcompatible);
	}
}
