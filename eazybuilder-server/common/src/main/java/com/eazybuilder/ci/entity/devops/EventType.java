package com.eazybuilder.ci.entity.devops;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EventType {

    AUTO_DEPLOYMENT("自动部署"),
    TRIGGER_AUTO_TEST("触发自动化测试"),
    /**
     * 发版
     */
    PUBLISH("发版"),

    push("代码提交"),

    merge("分支合并成功"),

    mergeRequest("申请分支合并"),

    pushTag("打标签"),
	
    applyOnlineAllowed("申请上线通过"),
	
	applyOntestAllowed("申请提测通过");

    private  String name;

    EventType(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
