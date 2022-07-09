package com.eazybuilder.ci.dto;

import com.google.common.collect.Maps;
import com.eazybuilder.ci.constant.MetricType;
import com.eazybuilder.ci.entity.Metric;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.ProjectType;
import com.eazybuilder.ci.entity.ScmType;
import com.eazybuilder.ci.entity.report.Status;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class ProjectLastBuildInfo {
	 private String id;
	 private String name;
	 private String jobName;
	 private String description;
	 private ScmType scmType;
	 private String scmTag;
	 private String jenkinsUrl;
	 private String sonarUrl;
	 private String sonarKey;
	 private String profileId;
	 private String profileName;
	 private Date lastBuild;
	 private Date lastUpdate;
	 private String triggerUserName;
	 private String triggerUserId;
	 private Status buildStatus;
	 private String lastLogId;
	 private String lastPipelineId;
	 private String sourceBranch;
	 private String targetBranch;
	 private ProjectType projectType;
	 private Map<MetricType,Metric> metrics=Maps.newConcurrentMap();
	 
	 public static ProjectLastBuildInfo build(Project project) {
		 ProjectLastBuildInfo build=new ProjectLastBuildInfo();
		 build.setId(project.getId());
		 build.setName(project.getName());
		 build.setDescription(project.getDescription());
		 build.setJobName(project.getJobName());
		 if(null!=project.getScm()) {
			 build.setScmType(project.getScm().getType());
//			 if(!StringUtils.isEmpty(project.getScm().getTagName())) {
//				 build.setScmTag(project.getScm().getTagName());
//			 }
			 if(!StringUtils.isEmpty(project.getScm().getSourceBranch())) {
				 build.setSourceBranch(project.getScm().getSourceBranch());
			 }
			 if(!StringUtils.isEmpty(project.getScm().getTargetBranch())) {
				 build.setTargetBranch(project.getScm().getTargetBranch());
			 }
		 }
		 build.setSonarKey(project.getSonarKey());
		 build.setProjectType(project.getProjectType());
		 return build;
	 }
	 
	 public static class ScmInfo{
		 private ScmType type;

		public ScmType getType() {
			return type;
		}

		public void setType(ScmType type) {
			this.type = type;
		}
	 }


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public String getJenkinsUrl() {
		return jenkinsUrl;
	}


	public void setJenkinsUrl(String jenkinsUrl) {
		this.jenkinsUrl = jenkinsUrl;
	}


	public String getSonarUrl() {
		return sonarUrl;
	}


	public void setSonarUrl(String sonarUrl) {
		this.sonarUrl = sonarUrl;
	}


	public Date getLastBuild() {
		return lastBuild;
	}


	public void setLastBuild(Date lastBuild) {
		this.lastBuild = lastBuild;
	}


	public String getTriggerUserName() {
		return triggerUserName;
	}


	public void setTriggerUserName(String triggerUserName) {
		this.triggerUserName = triggerUserName;
	}


	public String getTriggerUserId() {
		return triggerUserId;
	}


	public void setTriggerUserId(String triggerUserId) {
		this.triggerUserId = triggerUserId;
	}


	public String getProfileId() {
		return profileId;
	}


	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}


	public String getProfileName() {
		return profileName;
	}


	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public ScmType getScmType() {
		return scmType;
	}


	public void setScmType(ScmType scmType) {
		this.scmType = scmType;
	}


	public String getSonarKey() {
		return sonarKey;
	}


	public void setSonarKey(String sonarKey) {
		this.sonarKey = sonarKey;
	}


	public Status getBuildStatus() {
		return buildStatus;
	}


	public void setBuildStatus(Status buildStatus) {
		this.buildStatus = buildStatus;
	}


	public Date getLastUpdate() {
		return lastUpdate;
	}


	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}


	public Map<MetricType,Metric> getMetrics() {
		return metrics;
	}


	public void setMetrics(Map<MetricType,Metric> metrics) {
		this.metrics = metrics;
	}


	public void setMetricList(List<Metric> metricList){
		Map<MetricType,Metric>  map = Maps.newHashMap();
	 	if(metricList != null && metricList.size() > 0){
			for(Metric metric:metricList){
				map.put(metric.getType(),metric);
			}
		}
	 	this.setMetrics(map);
	}


	public String getLastLogId() {
		return lastLogId;
	}


	public void setLastLogId(String lastLogId) {
		this.lastLogId = lastLogId;
	}


	public String getLastPipelineId() {
		return lastPipelineId;
	}


	public void setLastPipelineId(String lastPipelineId) {
		this.lastPipelineId = lastPipelineId;
	}

	public String getScmTag() {
		return scmTag;
	}

	public void setScmTag(String scmTag) {
		this.scmTag = scmTag;
	}

	public String getSourceBranch() {
		return sourceBranch;
	}

	public void setSourceBranch(String sourceBranch) {
		this.sourceBranch = sourceBranch;
	}

	public String getTargetBranch() {
		return targetBranch;
	}

	public void setTargetBranch(String targetBranch) {
		this.targetBranch = targetBranch;
	}


	public String getJobName() {
		return jobName;
	}


	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}
}
