package com.eazybuilder.ci.entity;

public enum PipelineType {
	release,
	online,
	push,
	merge,
	manual,
	//定时任务
	job,
	//立即任务
	manualJob;
}
