package com.eazybuilder.ci.entity;

public enum JobTrigger {
	/**
	 * 时间计划触发
	 */
	cron,
	/**
	 * git web hook
	 */
	git_web_hook,
	/**
	 * 手工触发
	 */
	manual,
	/**
	 * 其他任务结束
	 */
	watch_job_executed;
}
