package com.eazybuilder.ci.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 审计视图设置(每个项目审计人员可以维护一个到多个审计视图、当审计人员查询质量报表时，
 * 可以通过审计视图定制筛选的工程项目)
 *
 */
@Entity
@Table(name="CI_AUDIT_PROFILE")
public class AuditProfile {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int")
	private String id;
	/**
	 * profileName
	 */
	private String profileName;
	
	private String description;
	/**
	 * 关联分组
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@NotFound(action=NotFoundAction.IGNORE)
	private List<ProjectGroup> groups;
	/**
	 * 所属审计人员
	 */
	private String ownerId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProfileName() {
		return profileName;
	}
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
}
