package com.eazybuilder.ci.entity.devops;

import java.util.Date;
import java.util.HashMap;

public class RedmineIssuesDetail {
    //@ApiModelProperty(value = "问题（需求）ID、需求码")
    private Integer id;
    //@ApiModelProperty(value = "被指派用户ID")
    private Integer assigneeId;
    //@ApiModelProperty(value = "被指派用户姓名")
    private String assigneeName;
    //@ApiModelProperty(value = "作者（问题发起人）ID")
    private Integer authorId;
    //@ApiModelProperty(value = "作者（问题发起人）姓名")
    private String authorName;
    //@ApiModelProperty(value = "问题（需求）描述")
    private String description;
    //@ApiModelProperty(value = "问题（需求）创建时间")
    private Date createOn;
    //@ApiModelProperty(value = "问题（需求）截止日期")
    private Date dueDate;
    //@ApiModelProperty(value = "问题（需求）关闭日期")
    private Date closedOn;
    //@ApiModelProperty(value = "问题（需求）完成百分比")
    private Integer doneRatio;
    //@ApiModelProperty(value = "预估时间")
    private Float estimatedHours;
    //@ApiModelProperty(value = "父问题（需求）ID")
    private Integer parentId;
    //@ApiModelProperty(value = "优先级ID")
    private Integer priorityId;
    //@ApiModelProperty(value = "优先级名称")
    private String priorityText;
    //@ApiModelProperty(value = "项目ID")
    private Integer projectId;
    //@ApiModelProperty(value = "项目名称")
    private String projectName;
    //@ApiModelProperty(value = "占用时间")
    private Float spentHours;
    //@ApiModelProperty(value = "开始日期")
    private Date startDate;
    //@ApiModelProperty(value = "任务状态ID")
    private Integer statusId;
    //@ApiModelProperty(value = "任务状态名称")
    private String statusName;
    //@ApiModelProperty(value = "主题")
    private String subject;
    //@ApiModelProperty(value = "更新时间")
    private Date updateOn;
    //@ApiModelProperty(value = "跟踪标签ID")
    private Integer trackerId;
    //@ApiModelProperty(value = "项目版本ID")
    private Integer versionId;
    //@ApiModelProperty(value = "问题类别ID")
    private Integer categoryId;
    //@ApiModelProperty(value = "自定义字段列表")
    private HashMap<String,String> customField;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getClosedOn() {
        return closedOn;
    }

    public void setClosedOn(Date closedOn) {
        this.closedOn = closedOn;
    }

    public Integer getDoneRatio() {
        return doneRatio;
    }

    public void setDoneRatio(Integer doneRatio) {
        this.doneRatio = doneRatio;
    }

    public Float getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(Float estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
    }

    public String getPriorityText() {
        return priorityText;
    }

    public void setPriorityText(String priorityText) {
        this.priorityText = priorityText;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Float getSpentHours() {
        return spentHours;
    }

    public void setSpentHours(Float spentHours) {
        this.spentHours = spentHours;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getUpdateOn() {
        return updateOn;
    }

    public void setUpdateOn(Date updateOn) {
        this.updateOn = updateOn;
    }

    public Integer getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(Integer trackerId) {
        this.trackerId = trackerId;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public HashMap<String, String> getCustomField() {
        return customField;
    }

    public void setCustomField(HashMap<String, String> customField) {
        this.customField = customField;
    }
}
