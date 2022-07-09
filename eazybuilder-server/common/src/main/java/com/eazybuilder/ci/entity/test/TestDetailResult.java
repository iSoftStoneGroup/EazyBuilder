package com.eazybuilder.ci.entity.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CI_TEST_DETAIL_RESULT")
public class TestDetailResult {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(10)")
	private String id;
	private String historyId;
	private Integer executeOrder;
	private String scriptName;
	private Integer totalPass;
	private Integer totalFailed;
	private Integer totalWarning;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHistoryId() {
		return historyId;
	}
	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}
	public Integer getExecuteOrder() {
		return executeOrder;
	}
	public void setExecuteOrder(Integer executeOrder) {
		this.executeOrder = executeOrder;
	}
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	public Integer getTotalPass() {
		return totalPass;
	}
	public void setTotalPass(Integer totalPass) {
		this.totalPass = totalPass;
	}
	public Integer getTotalFailed() {
		return totalFailed;
	}
	public void setTotalFailed(Integer totalFailed) {
		this.totalFailed = totalFailed;
	}
	public Integer getTotalWarning() {
		return totalWarning;
	}
	public void setTotalWarning(Integer totalWarning) {
		this.totalWarning = totalWarning;
	}
}
