package com.eazybuilder.ci.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name="CI_SCM")
public class Scm {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(5)")
	private String id;
	private String name;
	private ScmType type;
	private String url;
	private String tagName;

	/**
	 * 指定临时分支、不保存数据库
	 */
	@Transient
	private String arriveTagName;

	private String user;
	//password not visible for ui or any json read case
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	@Transient
	@JsonProperty(access = Access.READ_WRITE)
	private boolean changePwd;

	private String sourceBranch;
	private String targetBranch;

	public ScmType getType() {
		return type;
	}
	public void setType(ScmType type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public boolean isChangePwd() {
		return changePwd;
	}
	public void setChangePwd(boolean changePwd) {
		this.changePwd = changePwd;
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

	public String getArriveTagName() {
		return arriveTagName;
	}

	public void setArriveTagName(String arriveTagName) {
		this.arriveTagName = arriveTagName;
	}
}
