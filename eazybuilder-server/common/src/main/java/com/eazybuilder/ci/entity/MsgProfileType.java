package com.eazybuilder.ci.entity;


public enum MsgProfileType {


	/**
	 * 流水线通知类 钉钉推送
	 */
	pipelineDingTalk,
	/**
	 * 流水线通知类 仅失败发送
	 */
	 pipelineFail,

	/**
	 * 流水线通知类 红灯修复
	 */
	 focusRedlightRepair,

	/**
	 * 流水线通知类 正反正脚本执行确认
	 */
	 sqlScript,

	/**
	 * 自动化测试通知类 性能测试
	 */
	 dtpPerformanceTest,
	/**
	 * 自动化测试通知类 接口测试
	 */
	 dtpApiTest,
	/**
	 * 自动化测试通知类 WEB ui测试
	 */
	 dtpWebUiTest,
	/**
	 * 自动化测试通知类 安全测试
	 */
	 dtpSecurityTest,

	/**
	 * 自动化测试通知类
	 */

	/**
	 * gitlab事件类 代码提交
	 */
	 push,

	/**
	 * gitlab事件类 合并申请
	 */
	 mergeApply,

	/**
	 * gitlab事件类 合并关闭
	 */
	 mergeRefused,

	/**
	 * gitlab事件类 合并通过
	 */
	 mergePass,

	/**
	 * 提测类 申请提测提醒
	 */
	 releaseApply,
	/**
	 * 提测类 待审批提醒
	 */
	 releaseWait,
	/**
	 * 提测类 通过提醒
	 */
	 releasePass,
	/**
	 * 提测类 拒绝提醒
	 */
	 releaseRefused,

	/**
	 * 上线类 申请提测提醒
	 */
	 onlineApply,
	/**
	 * 提测类 待审批提醒
	 */
	 onlineWait,
	/**
	 * 提测类 通过提醒
	 */
	 onlinePass,
	/**
	 * 提测类 拒绝提醒
	 */
	 onlineRefused,

	/**
	 * 部署类 部署待执行提醒
	 */
	 deployWait,
	/**
	 * 部署类 部署已执行提醒
	 */
	 deployRun,

	/**
	 * 监控类 流水线触发自动化测试
	 */
	 monitoringDtpTestRun,

	/**
	 * 监控类 自动化测试成功/失败
	 */
	 monitoringDtpTestStatus,

	/**
	 * 监控类 度量数据每日同步提醒（成功/失败）/失败
	 */
	 monitoringMeasureDataSync,

	/**
	 * 监控类 定时任务执行提醒
	 */
	 monitoringJobRun,
	/**
	 * 监控类 度量报告
	 */
	monitoringMeasureReport,
	/**
	 * 监控类 需求三天未认领
	 */
	needToDoPass3Day,
	/**
	 * 监控类 敏感sql
	 */
	monitoringSql,


	/**
	 * 配置文件变更类 配置文件（开发，测试，生产）变更提醒
	 */
	 configEdit,
	/**
	 * redmine 任务状态更新
	 */
	issuesStatusUpdate,
	/**
	 * redmine 新增任务
	 */
	issuesAdd
}
