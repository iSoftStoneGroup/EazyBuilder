package com.eazybuilder.ci.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="CI_DTP_REPORT")
public class DtpReport implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(8)")
	private String id;

	private Integer envExceptionFailed;

	private String projectName;

	private Long elapsedTime;

	private Long createTime;

	private boolean end;

	private boolean succeed;

	private String name;

	private String testType;

	private String reportUrl;

	private String gitUrl;

	private String message;

	private String code;

	private String projectId;

	private String planId;

	private  String pipelineHistoryId;

	private Integer totalPass;

	private Integer totalFail;

	private Integer expectRun;


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

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getReportUrl() {
		return reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}

	public String getGitUrl() {
		return gitUrl;
	}

	public void setGitUrl(String gitUrl) {
		this.gitUrl = gitUrl;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSucceed() {
		return succeed;
	}

	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}

	public String getPipelineHistoryId() {
		return pipelineHistoryId;
	}

	public void setPipelineHistoryId(String pipelineHistoryId) {
		this.pipelineHistoryId = pipelineHistoryId;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public Integer getTotalPass() {
		return totalPass;
	}

	public void setTotalPass(Integer totalPass) {
		this.totalPass = totalPass;
	}

	public Integer getTotalFail() {
		return totalFail;
	}

	public void setTotalFail(Integer totalFail) {
		this.totalFail = totalFail;
	}

	public Integer getExpectRun() {
		return expectRun;
	}

	public void setExpectRun(Integer expectRun) {
		this.expectRun = expectRun;
	}

	public Integer getEnvExceptionFailed() {
		return envExceptionFailed;
	}

	public void setEnvExceptionFailed(Integer envExceptionFailed) {
		this.envExceptionFailed = envExceptionFailed;
	}

	@Override
	public String toString() {
		return "DtpReport{" +
				"id='" + id + '\'' +
				", envExceptionFailed=" + envExceptionFailed +
				", projectName='" + projectName + '\'' +
				", elapsedTime=" + elapsedTime +
				", createTime=" + createTime +
				", end=" + end +
				", succeed=" + succeed +
				", name='" + name + '\'' +
				", testType='" + testType + '\'' +
				", reportUrl='" + reportUrl + '\'' +
				", gitUrl='" + gitUrl + '\'' +
				", message='" + message + '\'' +
				", code='" + code + '\'' +
				", projectId='" + projectId + '\'' +
				", planId='" + planId + '\'' +
				", pipelineHistoryId='" + pipelineHistoryId + '\'' +
				", totalPass=" + totalPass +
				", totalFail=" + totalFail +
				", expectRun=" + expectRun +
				'}';
	}
}
