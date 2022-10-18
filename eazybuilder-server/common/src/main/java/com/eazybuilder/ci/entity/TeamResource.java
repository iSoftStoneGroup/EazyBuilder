package com.eazybuilder.ci.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 项目组对应的CI资源（用于指定项目专用的CI服务器）
 *
 *
 */
@Entity
@Table(name="CI_TEAM_RESOURCE")
public class TeamResource implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int")
	private String id;
	
	@Column(name="team_id",columnDefinition="int(8)")
	private String teamId;
	private String teamName;
	private String jenkinsUrl;
	@Column(name="jenkins_k8s_support",columnDefinition="int(1) default 0")
	private boolean jenkinsK8sSupport;
	private String sonarUrl;
	private String referenceSource;
	private String k8sYmlPath;
	private String jenkinsWorkPath;
	private String jenkinsWorkType;

	public String getJenkinsUrl() {
		return jenkinsUrl;
	}
	public void setJenkinsUrl(String jenkinsUrl) {
		this.jenkinsUrl = jenkinsUrl;
	}

	public String getJenkinsWorkPath() {
		return jenkinsWorkPath;
	}

	public void setJenkinsWorkPath(String jenkinsWorkPath) {
		this.jenkinsWorkPath = jenkinsWorkPath;
	}

	public String getJenkinsWorkType() {
		return jenkinsWorkType;
	}

	public void setJenkinsWorkType(String jenkinsWorkType) {
		this.jenkinsWorkType = jenkinsWorkType;
	}

	public String getSonarUrl() {
		return sonarUrl;
	}
	public void setSonarUrl(String sonarUrl) {
		this.sonarUrl = sonarUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public boolean isJenkinsK8sSupport() {
		return jenkinsK8sSupport;
	}
	public void setJenkinsK8sSupport(boolean jenkinsK8sSupport) {
		this.jenkinsK8sSupport = jenkinsK8sSupport;
	}

	public String getReferenceSource() {
		return referenceSource;
	}

	public void setReferenceSource(String referenceSource) {
		this.referenceSource = referenceSource;
	}

	public String getK8sYmlPath() {
		return k8sYmlPath;
	}

	public void setK8sYmlPath(String k8sYmlPath) {
		this.k8sYmlPath = k8sYmlPath;
	}
}
