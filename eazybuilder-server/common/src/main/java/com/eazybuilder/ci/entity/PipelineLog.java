package com.eazybuilder.ci.entity;

import javax.persistence.*;

import com.eazybuilder.ci.entity.report.Status;

@Entity
@Table(name = "CI_PIPELINE_LOG")
public class PipelineLog {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(8)")
	private String id;// : 
	private String name;//
	private Status status;//
//	事件来源，手工触发，事件触发，定时任务触发
	private ExecuteType executeType;	
	private String eventType;
	private String pipelineId;
	private String exceptionLog;
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public Status getStatus() {
		return status;
	}
	public ExecuteType getExecuteType() {
		return executeType;
	}
	public String getEventType() {
		return eventType;
	}
	public String getPipelineId() {
		return pipelineId;
	}
	public String getExceptionLog() {
		return exceptionLog;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public void setExecuteType(ExecuteType executeType) {
		this.executeType = executeType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public void setPipelineId(String pipelineId) {
		this.pipelineId = pipelineId;
	}
	public void setExceptionLog(String exceptionLog) {
		this.exceptionLog = exceptionLog;
	}
	
	
}