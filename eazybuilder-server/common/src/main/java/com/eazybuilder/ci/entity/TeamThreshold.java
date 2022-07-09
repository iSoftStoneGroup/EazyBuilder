package com.eazybuilder.ci.entity;

import com.eazybuilder.ci.constant.ActionScope;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity

@Table(name = "CI_TEAM_THRESHOLD")

public class TeamThreshold implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3502518548341018567L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "int(8)")
	private String id;

	private String teamId;

	private ActionScope actionScope;
	// 阈值类型
	private ThresholdType threSholdType;

	// 阈值id
	private String blockerId;

	private Date inputTime;

	private Date updateTime;
	private String remark1;
	private String remark2;
	private String remark3;

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

	public ThresholdType getThreSholdType() {
		return threSholdType;
	}

	public void setThreSholdType(ThresholdType threSholdType) {
		this.threSholdType = threSholdType;
	}

	public String getBlockerId() {
		return blockerId;
	}

	public void setBlockerId(String blockerId) {
		this.blockerId = blockerId;
	}

	public Date getInputTime() {
		return inputTime;
	}

	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

	public ActionScope getActionScope() {
		return actionScope;
	}

	public void setActionScope(ActionScope actionScope) {
		this.actionScope = actionScope;
	}
}
