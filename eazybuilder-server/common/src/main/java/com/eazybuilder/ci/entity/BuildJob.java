package com.eazybuilder.ci.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.eazybuilder.ci.constant.PreTaskStatus;
import com.eazybuilder.ci.entity.report.Status;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name="CI_JOB")
public class BuildJob implements Serializable {
	public  static final String ARRANGEMENT_JOB_REDIS_KEY_FORMATE = "ARRANGEMENT_JOB_%s";
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(8)")
	private String id;
	/**
	 * 任务名称
	 */
	private String name;
	private Status status;

	/**
	 * 批量构建的工程ID
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@NotFound(action=NotFoundAction.IGNORE)
	private List<Project> projects;



	/**
	 * 任务类型 是否属于 上线类型
	 */
	private boolean onLine;
	/**
	 * 是否紧急上线
	 */
	private boolean immedIatelyOnline;
	/**
	 * 上线id
	 */
	private String onLineId;


	/**
	 * 上线tag
	 */
	private String onlineTag;
	/**
	 * 任务所属项目组
	 */
	private String teamId;
	
	/**
	 * 仅在任务失败/构建失败时发送邮件
	 */
	private boolean sendMailOnFail;
	/**
	 * 如何触发
	 */
	private JobTrigger triggerType;

	/**
	 * 触发条件--前置任务执行状态
	 */
	private PreTaskStatus preTaskStatus;


	private String watchJobId;

	private String watchJobName;

	/**
	 * X-Gitlab-Token
	 */
	private String webHookToken;
	
	/**
	 * 时间计划
	 */
	private String cron;
	
	/**
	 * 上次触发时间
	 */
	private Long lastTrigger;
	
	/**
	 * 下次执行时间
	 */
	private Long nextTime;
	
	private String profileId;
	
	/**
	 * 是否质量管理任务(只能有audit角色设置)
	 */
	@Column(name="qa_job",columnDefinition="smallint default 0")
	private boolean qaJob;
	
	/**
	 * 是否发送钉钉通知
	 */
	@Column(name="notify_dingtalk",columnDefinition="smallint default 0")
	private boolean notifyDingtalk;
	/**
	 * 钉钉机器人webhook设置
	 */
	@OneToOne(cascade = CascadeType.ALL)
	private DingtalkWebhook dingtalkWebHook;

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
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	public Long getNextTime() {
		return nextTime;
	}
	public void setNextTime(Long nextTime) {
		this.nextTime = nextTime;
	}
	public Long getLastTrigger() {
		return lastTrigger;
	}
	public void setLastTrigger(Long lastTrigger) {
		this.lastTrigger = lastTrigger;
	}
	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	public List<Project> getProjects() {
		return projects;
	}
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public boolean isSendMailOnFail() {
		return sendMailOnFail;
	}
	public void setSendMailOnFail(boolean sendMailOnFail) {
		this.sendMailOnFail = sendMailOnFail;
	}
	public JobTrigger getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(JobTrigger triggerType) {
		this.triggerType = triggerType;
	}
	public String getWebHookToken() {
		return webHookToken;
	}
	public void setWebHookToken(String webHookToken) {
		this.webHookToken = webHookToken;
	}
	public DingtalkWebhook getDingtalkWebHook() {
		return dingtalkWebHook;
	}
	public void setDingtalkWebHook(DingtalkWebhook dingtalkWebHook) {
		this.dingtalkWebHook = dingtalkWebHook;
	}
	public boolean isNotifyDingtalk() {
		return notifyDingtalk;
	}
	public void setNotifyDingtalk(boolean notifyDingtalk) {
		this.notifyDingtalk = notifyDingtalk;
	}
	public boolean isQaJob() {
		return qaJob;
	}
	public void setQaJob(boolean qaJob) {
		this.qaJob = qaJob;
	}

	public boolean isOnLine() {
		return onLine;
	}

	public void setOnLine(boolean onLine) {
		this.onLine = onLine;
	}

	public String getOnLineId() {
		return onLineId;
	}

	public void setOnLineId(String onLineId) {
		this.onLineId = onLineId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isImmedIatelyOnline() {
		return immedIatelyOnline;
	}

	public void setImmedIatelyOnline(boolean immedIatelyOnline) {
		this.immedIatelyOnline = immedIatelyOnline;
	}


	public PreTaskStatus getPreTaskStatus() {
		return preTaskStatus;
	}

	public void setPreTaskStatus(PreTaskStatus preTaskStatus) {
		this.preTaskStatus = preTaskStatus;
	}

	public String getWatchJobId() {
		return watchJobId;
	}

	public void setWatchJobId(String watchJobId) {
		this.watchJobId = watchJobId;
	}

	public String getWatchJobName() {
		return watchJobName;
	}

	public void setWatchJobName(String watchJobName) {
		this.watchJobName = watchJobName;
	}

	public String getArrangementJobRedisKey(){
		return String.format(ARRANGEMENT_JOB_REDIS_KEY_FORMATE,watchJobId);
	}

	public static String getArrangementJobRedisKey(String preJobId){
		return String.format(ARRANGEMENT_JOB_REDIS_KEY_FORMATE,preJobId);
	}

	public String getOnlineTag() {
		return onlineTag;
	}

	public void setOnlineTag(String onlineTag) {
		this.onlineTag = onlineTag;
	}
}
