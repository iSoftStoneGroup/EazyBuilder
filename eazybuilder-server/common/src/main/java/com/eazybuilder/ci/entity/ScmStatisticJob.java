package com.eazybuilder.ci.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name="CI_SCM_STATISTIC_JOB",uniqueConstraints = @UniqueConstraint(columnNames = "repoUrl"))
public class ScmStatisticJob implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int")
	private String id;
	/**
	 * 名称
	 */
	private String name;
	
	private ScmType repoType;
	
	private String repoUrl;
	
	private String accessUser;
	
	private String accessToken;
	
	private boolean enable;

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

	public String getRepoUrl() {
		return repoUrl;
	}

	public void setRepoUrl(String repoUrl) {
		this.repoUrl = repoUrl;
	}

	public String getAccessUser() {
		return accessUser;
	}

	public void setAccessUser(String accessUser) {
		this.accessUser = accessUser;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public ScmType getRepoType() {
		return repoType;
	}

	public void setRepoType(ScmType repoType) {
		this.repoType = repoType;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}
