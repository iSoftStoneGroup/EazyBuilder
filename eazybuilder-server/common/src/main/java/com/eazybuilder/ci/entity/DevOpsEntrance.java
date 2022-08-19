package com.eazybuilder.ci.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 质量统计信息
 */
@Entity
@Table(name = "CI_DEVOPS_ENTRANCE")
public class DevOpsEntrance {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "int(10)")
	private String id;

	private String code;

	private String name;

	private Date inputeTime;

	private Date updateTime;

	private ValidStatus validStatus;

	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public Date getInputeTime() {
		return inputeTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public ValidStatus getValidStatus() {
		return validStatus;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setInputeTime(Date inputeTime) {
		this.inputeTime = inputeTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setValidStatus(ValidStatus validStatus) {
		this.validStatus = validStatus;
	}
	
	
	
}
