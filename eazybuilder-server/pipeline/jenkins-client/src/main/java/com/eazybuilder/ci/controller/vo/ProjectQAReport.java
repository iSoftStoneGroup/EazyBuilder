package com.eazybuilder.ci.controller.vo;

import com.eazybuilder.ci.entity.Metric;
import com.eazybuilder.ci.entity.Pipeline;
import com.eazybuilder.ci.entity.Project;

public class ProjectQAReport {
	private String groupName;
	private String groupLeader;
	private Project project;
	private Iterable<Metric> metrics;
	private Pipeline latestPipeline;
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public Iterable<Metric> getMetrics() {
		return metrics;
	}
	public void setMetrics(Iterable<Metric> metrics) {
		this.metrics = metrics;
	}
	public Pipeline getLatestPipeline() {
		return latestPipeline;
	}
	public void setLatestPipeline(Pipeline latestPipeline) {
		this.latestPipeline = latestPipeline;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupLeader() {
		return groupLeader;
	}
	public void setGroupLeader(String groupLeader) {
		this.groupLeader = groupLeader;
	}
}
