package com.eazybuilder.ci.entity;

import javax.persistence.*;

/**
 * 发送消息配置（用于精细化设置都有哪些操作需要发送钉钉、邮件消息）
 */

@Entity
@Table(name="CI_MAIL_MSG_PROFILE")
public class MailMsgProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "int(8)")
	private String id;
	/**
	 * redmine 任务状态更新
	 */
	private boolean issuesStatusUpdate;
	/**
	 * redmine 新增任务
	 */
	private boolean issuesAdd;
	/**
	 * 流水线通知类 仅失败发送
	 */
	private boolean pipelineFail;
	/**
	 * 流水线通知类 仅成功发送
	 */
	private boolean pipelineSuccess;
	/**
	 * 流水线通知类 红灯修复提醒间隔
	 */
	private String focusRedlightRepairConfig;
	/**
	 * 流水线通知类 正反正脚本执行确认
	 */
	private boolean sqlScript;


	/**
	 * 自动化测试通知类 性能测试
	 */
	private boolean dtpPerformanceTest;
	/**
	 * 自动化测试通知类 接口测试
	 */
	private boolean dtpApiTest;
	/**
	 * 自动化测试通知类 WEB ui测试
	 */
	private boolean dtpWebUiTest;
	/**
	 * 自动化测试通知类 安全测试
	 */
	private boolean dtpSecurityTest;

	/**
	 * 自动化测试通知类
	 */

	/**
	 * gitlab事件类 代码提交
	 */
	private boolean push;

	/**
	 * gitlab事件类 合并申请
	 */
	private boolean mergeApply;

	/**
	 * gitlab事件类 合并关闭
	 */
	private boolean mergeRefused;

	/**
	 * gitlab事件类 合并通过
	 */
	private boolean mergePass;

	/**
	 * 提测类 申请提测提醒
	 */
	private boolean releaseApply;
	/**
	 * 提测类 待审批提醒
	 */
	private boolean releaseWait;
	/**
	 * 提测类 通过提醒
	 */
	private boolean releasePass;
	/**
	 * 提测类 拒绝提醒
	 */
	private boolean releaseRefused;

	/**
	 * 上线类 申请提测提醒
	 */
	private boolean onlineApply;
	/**
	 * 提测类 待审批提醒
	 */
	private boolean onlineWait;
	/**
	 * 提测类 通过提醒
	 */
	private boolean onlinePass;
	/**
	 * 提测类 拒绝提醒
	 */
	private boolean onlineRefused;

	/**
	 * 部署类 部署待执行提醒
	 */
	private boolean deployWait;
	/**
	 * 部署类 部署已执行提醒
	 */
	private boolean deployRun;
	/**
	 * 监控类 敏感sql
	 */
	private boolean monitoringSql;
	/**
	 * 监控类 流水线触发自动化测试
	 */
	private boolean monitoringDtpTestRun;

	/**
	 * 监控类 自动化测试成功/失败
	 */
	private boolean monitoringDtpTestStatus;

	/**
	 * 监控类 度量数据每日同步提醒（成功/失败）/失败
	 */
	private boolean monitoringMeasureDataSync;

	/**
	 * 监控类 定时任务执行提醒
	 */
	private boolean monitoringJobRun;
	/**
	 * 监控类 需求三天未认领
	 */
	private boolean needToDoPass3Day;

	/**
	 * 配置文件变更类 配置文件（开发，测试，生产）变更提醒
	 */
	private boolean configEdit;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isPipelineFail() {
		return pipelineFail;
	}

	public void setPipelineFail(boolean pipelineFail) {
		this.pipelineFail = pipelineFail;
	}


	public boolean isSqlScript() {
		return sqlScript;
	}

	public void setSqlScript(boolean sqlScript) {
		this.sqlScript = sqlScript;
	}

	public boolean isPush() {
		return push;
	}

	public void setPush(boolean push) {
		this.push = push;
	}

	public boolean isMergeApply() {
		return mergeApply;
	}

	public void setMergeApply(boolean mergeApply) {
		this.mergeApply = mergeApply;
	}

	public boolean isMergeRefused() {
		return mergeRefused;
	}

	public void setMergeRefused(boolean mergeRefused) {
		this.mergeRefused = mergeRefused;
	}

	public boolean isMergePass() {
		return mergePass;
	}

	public void setMergePass(boolean mergePass) {
		this.mergePass = mergePass;
	}

	public boolean isReleaseApply() {
		return releaseApply;
	}

	public void setReleaseApply(boolean releaseApply) {
		this.releaseApply = releaseApply;
	}

	public boolean isReleaseWait() {
		return releaseWait;
	}

	public void setReleaseWait(boolean releaseWait) {
		this.releaseWait = releaseWait;
	}

	public boolean isReleasePass() {
		return releasePass;
	}

	public void setReleasePass(boolean releasePass) {
		this.releasePass = releasePass;
	}

	public boolean isReleaseRefused() {
		return releaseRefused;
	}

	public void setReleaseRefused(boolean releaseRefused) {
		this.releaseRefused = releaseRefused;
	}

	public boolean isOnlineApply() {
		return onlineApply;
	}

	public void setOnlineApply(boolean onlineApply) {
		this.onlineApply = onlineApply;
	}

	public boolean isOnlineWait() {
		return onlineWait;
	}

	public void setOnlineWait(boolean onlineWait) {
		this.onlineWait = onlineWait;
	}

	public boolean isOnlinePass() {
		return onlinePass;
	}

	public void setOnlinePass(boolean onlinePass) {
		this.onlinePass = onlinePass;
	}

	public boolean isOnlineRefused() {
		return onlineRefused;
	}

	public void setOnlineRefused(boolean onlineRefused) {
		this.onlineRefused = onlineRefused;
	}

	public boolean isDeployWait() {
		return deployWait;
	}

	public void setDeployWait(boolean deployWait) {
		this.deployWait = deployWait;
	}

	public boolean isDeployRun() {
		return deployRun;
	}

	public void setDeployRun(boolean deployRun) {
		this.deployRun = deployRun;
	}

	public boolean isMonitoringDtpTestRun() {
		return monitoringDtpTestRun;
	}

	public void setMonitoringDtpTestRun(boolean monitoringDtpTestRun) {
		this.monitoringDtpTestRun = monitoringDtpTestRun;
	}

	public boolean isMonitoringDtpTestStatus() {
		return monitoringDtpTestStatus;
	}

	public void setMonitoringDtpTestStatus(boolean monitoringDtpTestStatus) {
		this.monitoringDtpTestStatus = monitoringDtpTestStatus;
	}

	public boolean isMonitoringMeasureDataSync() {
		return monitoringMeasureDataSync;
	}

	public void setMonitoringMeasureDataSync(boolean monitoringMeasureDataSync) {
		this.monitoringMeasureDataSync = monitoringMeasureDataSync;
	}

	public boolean isMonitoringJobRun() {
		return monitoringJobRun;
	}

	public void setMonitoringJobRun(boolean monitoringJobRun) {
		this.monitoringJobRun = monitoringJobRun;
	}

	public boolean isConfigEdit() {
		return configEdit;
	}

	public void setConfigEdit(boolean configEdit) {
		this.configEdit = configEdit;
	}

	public boolean isDtpPerformanceTest() {
		return dtpPerformanceTest;
	}

	public void setDtpPerformanceTest(boolean dtpPerformanceTest) {
		this.dtpPerformanceTest = dtpPerformanceTest;
	}

	public boolean isDtpApiTest() {
		return dtpApiTest;
	}

	public void setDtpApiTest(boolean dtpApiTest) {
		this.dtpApiTest = dtpApiTest;
	}

	public boolean isDtpWebUiTest() {
		return dtpWebUiTest;
	}

	public void setDtpWebUiTest(boolean dtpWebUiTest) {
		this.dtpWebUiTest = dtpWebUiTest;
	}

	public boolean isDtpSecurityTest() {
		return dtpSecurityTest;
	}

	public void setDtpSecurityTest(boolean dtpSecurityTest) {
		this.dtpSecurityTest = dtpSecurityTest;
	}

	public boolean isIssuesStatusUpdate() {
		return issuesStatusUpdate;
	}

	public void setIssuesStatusUpdate(boolean issuesStatusUpdate) {
		this.issuesStatusUpdate = issuesStatusUpdate;
	}

	public boolean isIssuesAdd() {
		return issuesAdd;
	}

	public void setIssuesAdd(boolean issuesAdd) {
		this.issuesAdd = issuesAdd;
	}

	public boolean isMonitoringSql() {
		return monitoringSql;
	}

	public void setMonitoringSql(boolean monitoringSql) {
		this.monitoringSql = monitoringSql;
	}

	public boolean isNeedToDoPass3Day() {
		return needToDoPass3Day;
	}

	public void setNeedToDoPass3Day(boolean needToDoPass3Day) {
		this.needToDoPass3Day = needToDoPass3Day;
	}

	public String getFocusRedlightRepairConfig() {
		return focusRedlightRepairConfig;
	}

	public void setFocusRedlightRepairConfig(String focusRedlightRepairConfig) {
		this.focusRedlightRepairConfig = focusRedlightRepairConfig;
	}

	public boolean isPipelineSuccess() {
		return pipelineSuccess;
	}

	public void setPipelineSuccess(boolean pipelineSuccess) {
		this.pipelineSuccess = pipelineSuccess;
	}
}
