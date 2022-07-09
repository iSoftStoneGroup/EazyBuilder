package com.eazybuilder.ci.entity.devops;

import com.eazybuilder.ci.entity.ProjectType;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name="CI_EVENT")
public class Event implements Serializable{




	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(columnDefinition="int(8)")
	private String id;

	/**
	 * 项目组id
	 */
//	@ManyToOne(cascade=CascadeType.ALL)
	private String teamId;

	/*
	 * 事件类型 暂定push,merge,tag
	 */
	private EventType eventType;

	/**
	 * 对应 本次操作人的id
	 */
	private Long userId;

	/**
	 * 对应 本次操作人的姓名
	 */
	private String userName;
	/**
	 *工程类型
	 */
	private ProjectType projectType;

	//ANT 项目
	@Column(name="legacy_project",columnDefinition="int(1) default 0")
	private boolean legacyProject;

	/**
	 * 环境
	 */
	private  EnvironmentType environmentType;
	/**
	 * 是否默认
	 */
	private boolean isDefault;

	/**
	 * 执行流水线
	 */
//	@ManyToOne(cascade=CascadeType.ALL)
//	@NotFound(action=NotFoundAction.IGNORE)
	private String  profileId;

	/**
	 * 事件描述
	 */
	private String detail;
	/**
	 * 来源分支、支持通配符
	 */
	private String sourceBranch;

	/**
	 * 目标分支、支持通配符
	 */
	private String targetBranch;

	/**
	 * 创建时间
	 */
	private Date createDate;


	@Override
	public String toString() {
		return "Event{" +
				"id='" + id + '\'' +
				", teamId='" + teamId + '\'' +
				", eventType=" + eventType +
				", userId=" + userId +
				", userName='" + userName + '\'' +
				", projectType=" + projectType +
				", environmentType=" + environmentType +
				", isDefault=" + isDefault +
				", profileId='" + profileId + '\'' +
				", detail='" + detail + '\'' +
				", sourceBranch='" + sourceBranch + '\'' +
				", targetBranch='" + targetBranch + '\'' +
				", createDate=" + createDate +
				'}';
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean aDefault) {
		isDefault = aDefault;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public EnvironmentType getEnvironmentType() {
		return environmentType;
	}

	public void setEnvironmentType(EnvironmentType environmentType) {
		this.environmentType = environmentType;
	}

	public boolean isLegacyProject() {
		return legacyProject;
	}

	public void setLegacyProject(boolean legacyProject) {
		this.legacyProject = legacyProject;
	}
}
