package com.eazybuilder.ci.entity.report;

public enum Status {
	NOT_EXECUTED,
    ABORTED,
    SUCCESS,
    IN_PROGRESS,
    PAUSED_PENDING_INPUT,
    FAILED,
    UNSTABLE,
	ASSERT_WARNRULE_FAILED,
	WAIT_AUTO_TEST_RESULT,
	AUTO_TEST_FAIL;

	
	
	public boolean isFinished(){
		switch(this){
		case ABORTED:
		case SUCCESS:
		case FAILED:
		case UNSTABLE:
		case ASSERT_WARNRULE_FAILED:
		return true;
		default:
			return false;
		}
	}
	
	public boolean isSuccess(){
		switch(this){
		case SUCCESS:
		case UNSTABLE:
			return true;
		default:
			return false;
		}
	}
	
	public String toString(){
		switch(this){
		case ABORTED:
			return "取消";
		case FAILED:
			return "失败";
		case IN_PROGRESS:
			return "进行中";
		case NOT_EXECUTED:
			return "尚未执行";
		case PAUSED_PENDING_INPUT:
			return "暂停（等待输入指令）";
		case SUCCESS:
			return "成功";
		case UNSTABLE:
			return "成功(不稳定)";
		case ASSERT_WARNRULE_FAILED:
			return "门禁判断失败";
		case WAIT_AUTO_TEST_RESULT:
			return "等待自动化测试结果";
		case AUTO_TEST_FAIL:
			return "自动化测试失败";			
		default:
			return "失败";
		}
	}
}
