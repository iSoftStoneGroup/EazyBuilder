package com.eazybuilder.ci.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity

@Table(name = "CI_TEAM_NAMESPACE")
public class TeamNamespace extends BaseEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3502518548341018567L;

	// 命名空间代码
	private String code;
	// 命名空间名称
	private String name;

	private NamespaceType namespaceType;

	private String gitlabApiDomain;
	
	private String remark1;
	private String remark2;
	private String remark3;

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGitlabApiDomain() {
		return gitlabApiDomain;
	}

	public void setGitlabApiDomain(String gitlabApiDomain) {
		this.gitlabApiDomain = gitlabApiDomain;
	}

	public NamespaceType getNamespaceType() {
		return namespaceType;
	}

	public void setNamespaceType(NamespaceType namespaceType) {
		this.namespaceType = namespaceType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getRemark3() {
		return remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

}
