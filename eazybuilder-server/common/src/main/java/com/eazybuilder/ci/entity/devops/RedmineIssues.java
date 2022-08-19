package com.eazybuilder.ci.entity.devops;

import java.io.Serializable;

public class RedmineIssues implements Serializable {

    private Integer releaseId;
    private Integer releaseParentId;
    private Integer id;
    private Integer parentId;
    private String trackerName;
    private String subject;

    private String description;

    private Integer projectId;

    private String projectName;

    private String projectIdentifier;

    private String subProjectIdentifier;
    
    private String gitlabPath;
    
    

    public String getGitlabPath() {
		return gitlabPath;
	}

	public void setGitlabPath(String gitlabPath) {
		this.gitlabPath = gitlabPath;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectIdentifier() {
        return projectIdentifier;
    }

    public void setProjectIdentifier(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }

    public String getTrackerName() {
        return trackerName;
    }

    public void setTrackerName(String trackerName) {
        this.trackerName = trackerName;
    }

    public String getSubProjectIdentifier() {
        return subProjectIdentifier;
    }

    public void setSubProjectIdentifier(String subProjectIdentifier) {
        this.subProjectIdentifier = subProjectIdentifier;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(Integer releaseId) {
        this.releaseId = releaseId;
    }

    public Integer getReleaseParentId() {
        return releaseParentId;
    }

    public void setReleaseParentId(Integer releaseParentId) {
        this.releaseParentId = releaseParentId;
    }
}
