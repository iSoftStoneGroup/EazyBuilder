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

	public String toString(){
		switch(this){
			case release:
				return "提测";
			case online:
				return "上线";
			case push:
				return "代码提交";
			case merge:
				return "分支合并";
			case manual:
				return "手动触发";
			case job:
				return "定时任务";
			case manualJob:
				return "定时任务-手动触发";
			default:
				return "其它";
		}
	}
}
