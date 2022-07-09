package com.eazybuilder.ci.entity.devops;

public enum Status {
    NOT_EXECUTED,
    ABORTED,
    SUCCESS,
    IN_PROGRESS,
    PAUSED_PENDING_INPUT,
    FAILED,
    UNSTABLE,
    ON_LINE_IN;


    public boolean isFinished(){
        switch(this){
            case ABORTED:
            case SUCCESS:
            case FAILED:
            case UNSTABLE:
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
            case ON_LINE_IN:
                return "上线中";
            default:
                return "失败";
        }
    }
}
