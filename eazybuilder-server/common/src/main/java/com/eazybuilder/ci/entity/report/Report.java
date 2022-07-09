package com.eazybuilder.ci.entity.report;

public class Report {
	@Override
	public String toString() {
		return "Report{" +
				"name='" + name + '\'' +
				", type=" + type +
				", status=" + status +
				", summary=" + summary +
				", output='" + output + '\'' +
				", attachmentId='" + attachmentId + '\'' +
				", link='" + link + '\'' +
				", sonarKey='" + sonarKey + '\'' +
				", projectTagName='" + projectTagName + '\'' +
				", projectUrl='" + projectUrl + '\'' +
				'}';
	}

	private String name;
	private Type type;
	private Status status;
	private Summary summary;
	private String output;
	private String attachmentId;
	private String link="#";
	private String sonarKey;
	
	private String projectTagName;
	private String projectUrl;
	
	
	
	public String getProjectTagName() {
		return projectTagName;
	}

	public void setProjectTagName(String projectTagName) {
		this.projectTagName = projectTagName;
	}

	public String getProjectUrl() {
		return projectUrl;
	}

	public void setProjectUrl(String projectUrl) {
		this.projectUrl = projectUrl;
	}

	public static Report create(String name){
		Report report=new Report();
		report.setName(name);
		return report;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Summary getSummary() {
		return summary;
	}
	public void setSummary(Summary summary) {
		this.summary = summary;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSonarKey() {
		return sonarKey;
	}

	public void setSonarKey(String sonarKey) {
		this.sonarKey = sonarKey;
	}
}
