package com.eazybuilder.ci.entity.devops;

import javax.persistence.Column;
import java.util.Date;

public class RedmineSprint {

    private static final long serialVersionUID = -1L;


    private String id;

    private String name;

    private String description;

    @Column(columnDefinition="datetime")
    private Date sprintStartDate;

    @Column(columnDefinition="datetime")
    private Date sprintEndDate;

    private String userId;

    private String projectId;

    @Column(columnDefinition="datetime")
    private Date createdOn;

    @Column(columnDefinition="datetime")
    private Date updatedOn;

    @Column(columnDefinition = "int(1) default 1")
    private Boolean isProductBacklog;

    private String status;

    @Column(columnDefinition = "int(1) default 1")
    private Boolean shared;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getSprintStartDate() {
        return sprintStartDate;
    }

    public void setSprintStartDate(Date sprintStartDate) {
        this.sprintStartDate = sprintStartDate;
    }

    public Date getSprintEndDate() {
        return sprintEndDate;
    }

    public void setSprintEndDate(Date sprintEndDate) {
        this.sprintEndDate = sprintEndDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Boolean getProductBacklog() {
        return isProductBacklog;
    }

    public void setProductBacklog(Boolean productBacklog) {
        isProductBacklog = productBacklog;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }
}
