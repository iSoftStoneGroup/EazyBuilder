package com.eazybuilder.ci.controller.vo;

import com.eazybuilder.ci.entity.ScmType;
/**
 * 用于批量导入工程信息的VO
 *
 *
 */
public class ScmVO {
	private String id;
	private String name;
	private ScmType type;
	private String url;
	private String tagName;
	private String user;
	private String password;
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
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
