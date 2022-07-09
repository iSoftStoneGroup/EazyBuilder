package com.eazybuilder.ci.entity;

public enum JobTrigger {
	cron,//时间计划触发
	git_web_hook,//git web hook
	manual//手工触发
}
