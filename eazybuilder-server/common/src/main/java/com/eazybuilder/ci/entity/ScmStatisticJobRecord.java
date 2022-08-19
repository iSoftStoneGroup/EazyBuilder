package com.eazybuilder.ci.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * 记录JOB对应的执行结果
 *
 *
 */
@Entity
@Table(name="CI_SCM_STATISTIC_RECORD")
public class ScmStatisticJobRecord implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int")
	private String id;
	
	@ManyToOne
	private ScmStatisticJob job;
	
	private String day;
	
	private boolean success;
	
	private String errMsg;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ScmStatisticJob getJob() {
		return job;
	}

	public void setJob(ScmStatisticJob job) {
		this.job = job;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	
}
