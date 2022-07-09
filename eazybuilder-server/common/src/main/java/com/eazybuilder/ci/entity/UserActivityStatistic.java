package com.eazybuilder.ci.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
@Entity
@Table(name="CI_USER_STATISTIC",uniqueConstraints = @UniqueConstraint(columnNames = {"day","email","projectName","groupName"}))
public class UserActivityStatistic implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4831672715136626919L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int")
	private String id;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * email
	 */
	private String email;
	/**
	 * 工程所属分组
	 */
	private String groupName;
	/**
	 * 工程项目
	 */
	private String projectName;

	/**
	 * git lab path
	 */
	private String nameSpace;
	/**
	 * CI工程ID
	 */
	private String projectId;
	/**
	 * 提交数
	 */
	private int pushed;
	/**
	 * 增加的代码行数
	 */
	private int additions;
	/**
	 * 删除的代码行数
	 */
	private int deletions;
	/**
	 * 创建的Merge Request数
	 */
	private int openedMRs;
	/**
	 * 合并的Merge Request数
	 */
	private int mergedMRs;
	/**
	 * 统计日期(YYYY-MM-DD)
	 */
	private String day;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public int getPushed() {
		return pushed;
	}
	public void setPushed(int pushed) {
		this.pushed = pushed;
	}
	public int getOpenedMRs() {
		return openedMRs;
	}
	public void setOpenedMRs(int openedMRs) {
		this.openedMRs = openedMRs;
	}
	public int getMergedMRs() {
		return mergedMRs;
	}
	public void setMergedMRs(int mergedMRs) {
		this.mergedMRs = mergedMRs;
	}
	public int getAdditions() {
		return additions;
	}
	public void setAdditions(int additions) {
		this.additions = additions;
	}
	public int getDeletions() {
		return deletions;
	}
	public void setDeletions(int deletions) {
		this.deletions = deletions;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	@Override
	public String toString() {
		return "UserActivityStatistic [email=" + email + ", groupName=" + groupName + ", projectName=" + projectName
				+ ", pushed=" + pushed + ", additions=" + additions + ", deletions=" + deletions + ", openedMRs="
				+ openedMRs + ", mergedMRs=" + mergedMRs + ", day=" + day + "]";
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}
}
