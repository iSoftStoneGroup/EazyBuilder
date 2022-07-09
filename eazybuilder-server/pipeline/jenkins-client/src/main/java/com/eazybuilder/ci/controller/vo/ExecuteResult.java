package com.eazybuilder.ci.controller.vo;

public class ExecuteResult {

	private boolean success;
	private String remindMsg;
	
	public ExecuteResult() {
	}
	
	public ExecuteResult(boolean success, String remindMsg) {
		this.success = success;
		this.remindMsg = remindMsg;
	}
	
	public static ExecuteResult success(){
		return new ExecuteResult(true, "脚本执行中");
	}
	
	public static ExecuteResult failed(String msg){
		return new ExecuteResult(false, msg);
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getRemindMsg() {
		return remindMsg;
	}
	public void setRemindMsg(String remindMsg) {
		this.remindMsg = remindMsg;
	}
}
