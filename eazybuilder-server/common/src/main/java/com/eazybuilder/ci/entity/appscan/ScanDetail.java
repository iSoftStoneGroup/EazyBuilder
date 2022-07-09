package com.eazybuilder.ci.entity.appscan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="CI_APPSCAN_HISTORY_DETAIL")
public class ScanDetail {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(10)")
	private String id;
	private String historyId;
	private String scriptName;
	private Integer executeOrder;
	private Integer totalHigh=0;
	private Integer totalMedium=0;
	private Integer totalLow=0;
	private Integer totalInformational=0;
	
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
	public Integer getTotalHigh() {
		return totalHigh;
	}
	public void setTotalHigh(Integer totalHigh) {
		this.totalHigh = totalHigh;
	}
	public Integer getTotalMedium() {
		return totalMedium;
	}
	public void setTotalMedium(Integer totalMedium) {
		this.totalMedium = totalMedium;
	}
	public Integer getTotalLow() {
		return totalLow;
	}
	public void setTotalLow(Integer totalLow) {
		this.totalLow = totalLow;
	}
	public Integer getTotalInformational() {
		return totalInformational;
	}
	public void setTotalInformational(Integer totalInformational) {
		this.totalInformational = totalInformational;
	}
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
}
