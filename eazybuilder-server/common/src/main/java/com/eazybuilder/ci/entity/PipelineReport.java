package com.eazybuilder.ci.entity;

import java.util.List;

import com.eazybuilder.ci.entity.report.Report;

public class PipelineReport {
	private Project project;
	private List<Report> stageReport;
	
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public List<Report> getStageReport() {
		return stageReport;
	}
	public void setStageReport(List<Report> stageReport) {
		this.stageReport = stageReport;
	}
}
