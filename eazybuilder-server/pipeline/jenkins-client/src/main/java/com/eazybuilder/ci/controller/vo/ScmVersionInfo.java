package com.eazybuilder.ci.controller.vo;

import java.util.Date;
import java.util.List;

public class ScmVersionInfo {
	private String version;
	private Date date;
	private String author;
	private String log;
	private List<String> changelist;
	
	public ScmVersionInfo() {
	}


	public ScmVersionInfo(String version, Date date, String author, String log, List<String> changelist) {
		this.version = version;
		this.date = date;
		this.author = author;
		this.log = log;
		this.changelist = changelist;
	}



	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<String> getChangelist() {
		return changelist;
	}

	public void setChangelist(List<String> changelist) {
		this.changelist = changelist;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}


	public String getLog() {
		return log;
	}


	public void setLog(String log) {
		this.log = log;
	}
	
}
