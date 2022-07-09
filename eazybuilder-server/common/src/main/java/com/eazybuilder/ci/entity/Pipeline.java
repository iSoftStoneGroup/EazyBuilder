package com.eazybuilder.ci.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.eazybuilder.ci.entity.report.StatusGuard;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.eazybuilder.ci.entity.report.Stage;
import com.eazybuilder.ci.entity.report.Status;
@Entity
@Table(name="CI_PIPELINE_HISTORY")
public class Pipeline {
	@Id
	private String id;//: "2014-10-16_13-07-52",
	private String name;//": "#16",
	private Status status;//": "PAUSED_PENDING_INPUT",
	private long startTimeMillis;//": 1413461275770,
	private long endTimeMillis;//": 1413461285999,
	private long durationMillis;//": 10229,
	private String rolloutVersion;
	private Date releaseDate;
	private String nameSpace;
	private String jobId;
	private String email;

	/**
	 * 判断是否是前端项目
	 * 因为目前前端项目我们都是配置的maven结构，所以在页面上选择的是java类型
	 * 这里通过读取日志的方式判断项目实际类型是不是前端的。
	 */
    private boolean npmInstal;

	/**
	 * 是否包含自动化测试任务
	 */
    private boolean  dtpTask;


	/**
	 * 触发类型，提测、上线、其它、。
	 */
	 private PipelineType  pipelineType;

	/**
	 * 版本号，对应提测、上线的版本
	 */
	 private String pipelineVersion;

	private String ciPackageId;
	/**
	 * 来源分支、支持通配符
	 */
	private String sourceBranch;

	/**
	 * 目标分支、支持通配符
	 */
	private String targetBranch;
	
	private String runBranch;

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<Stage> stages;//":
	private String projectName;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "PROJECT_ID",foreignKey = @ForeignKey(value=ConstraintMode.NO_CONSTRAINT))  
	@NotFound(action=NotFoundAction.IGNORE)
	private Project project;
	
	private String scmVersion;
	
	private String logId;
	 
	//构建过程
	private String profileName;
	
	//构建过程id
	private String profileId;
	//自动化测试结果
	@OneToMany(fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<DtpReport> dtpReports;


	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="PIPELINE_ID",foreignKey = @ForeignKey(value=ConstraintMode.NO_CONSTRAINT))
	@NotFound(action=NotFoundAction.IGNORE)
	private List<Metric> metrics;

	private boolean assertThresholdFaild;

	private StatusGuard statusGuard;


	private Integer ciRepairAlertLevelEnum;

	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	public String getProfileName() {
		return profileName;
	}
	public String getRunBranch() {
		return runBranch;
	}
	public void setRunBranch(String runBranch) {
		this.runBranch = runBranch;
	}
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

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
	public long getStartTimeMillis() {
		return startTimeMillis;
	}
	public void setStartTimeMillis(long startTimeMillis) {
		this.startTimeMillis = startTimeMillis;
	}
	public long getEndTimeMillis() {
		return endTimeMillis;
	}
	public void setEndTimeMillis(long endTimeMillis) {
		this.endTimeMillis = endTimeMillis;
	}
	public long getDurationMillis() {
		return durationMillis;
	}
	public void setDurationMillis(long durationMillis) {
		this.durationMillis = durationMillis;
	}
	public List<Stage> getStages() {
		return stages;
	}
	public void setStages(List<Stage> stages) {
		this.stages = stages;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public String getScmVersion() {
		return scmVersion;
	}
	public void setScmVersion(String scmVersion) {
		this.scmVersion = scmVersion;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getSourceBranch() {
		return sourceBranch;
	}

	public void setSourceBranch(String sourceBranch) {
		this.sourceBranch = sourceBranch;
	}

	public String getTargetBranch() {
		return targetBranch;
	}

	public void setTargetBranch(String targetBranch) {
		this.targetBranch = targetBranch;
	}


	public List<Metric> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}


	public String getCiPackageId() {
		return ciPackageId;
	}

	public void setCiPackageId(String ciPackageId) {
		this.ciPackageId = ciPackageId;
	}

	public List<DtpReport> getDtpReports() {
		return dtpReports;
	}

	public void setDtpReports(List<DtpReport> dtpReports) {
		this.dtpReports = dtpReports;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public boolean isAssertThresholdFaild() {
		return assertThresholdFaild;
	}

	public void setAssertThresholdFaild(boolean assertThresholdFaild) {
		this.assertThresholdFaild = assertThresholdFaild;
	}

	public String getRolloutVersion() {
		return rolloutVersion;
	}

	public void setRolloutVersion(String rolloutVersion) {
		this.rolloutVersion = rolloutVersion;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public PipelineType getPipelineType() {
		return pipelineType;
	}

	public void setPipelineType(PipelineType pipelineType) {
		this.pipelineType = pipelineType;
	}

	public String getPipelineVersion() {
		return pipelineVersion;
	}

	public void setPipelineVersion(String pipelineVersion) {
		this.pipelineVersion = pipelineVersion;
	}


	public boolean isNpmInstal() {
		return npmInstal;
	}

	public void setNpmInstal(boolean npmInstal) {
		this.npmInstal = npmInstal;
	}

	public boolean isDtpTask() {
		return dtpTask;
	}

	public void setDtpTask(boolean dtpTask) {
		this.dtpTask = dtpTask;
	}

	public StatusGuard getStatusGuard() {
		return statusGuard;
	}

	public void setStatusGuard(StatusGuard statusGuard) {
		this.statusGuard = statusGuard;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getCiRepairAlertLevelEnum() {
		return ciRepairAlertLevelEnum;
	}

	public void setCiRepairAlertLevelEnum(Integer ciRepairAlertLevelEnum) {
		this.ciRepairAlertLevelEnum = ciRepairAlertLevelEnum;
	}

	@Override
	public String toString() {
		return "Pipeline{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", status=" + status +
				", startTimeMillis=" + startTimeMillis +
				", endTimeMillis=" + endTimeMillis +
				", durationMillis=" + durationMillis +
				", rolloutVersion='" + rolloutVersion + '\'' +
				", releaseDate=" + releaseDate +
				", nameSpace='" + nameSpace + '\'' +
				", npmInstal=" + npmInstal +
				", dtpTask=" + dtpTask +
				", pipelineType=" + pipelineType +
				", pipelineVersion='" + pipelineVersion + '\'' +
				", ciPackageId='" + ciPackageId + '\'' +
				", sourceBranch='" + sourceBranch + '\'' +
				", targetBranch='" + targetBranch + '\'' +
				", runBranch='" + runBranch + '\'' +
				", stages=" + stages +
				", projectName='" + projectName + '\'' +
				", project=" + project +
				", scmVersion='" + scmVersion + '\'' +
				", logId='" + logId + '\'' +
				", profileName='" + profileName + '\'' +
				", profileId='" + profileId + '\'' +
				", dtpReports=" + dtpReports +
				", metrics=" + metrics +
				", assertThresholdFaild=" + assertThresholdFaild +
				", statusGuard=" + statusGuard +
				'}';
	}
}