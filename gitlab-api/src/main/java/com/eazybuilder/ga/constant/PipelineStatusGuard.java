package com.eazybuilder.ga.constant;

public enum PipelineStatusGuard {
    NOT_EXECUTED,
    SUCCESS,
    FAILED;
    public String toString(){
        switch(this){
            case FAILED:
                return "失败";
            case NOT_EXECUTED:
                return "尚未执行";
            case SUCCESS:
                return "成功";
            default:
                return "失败";
        }
    }
}
