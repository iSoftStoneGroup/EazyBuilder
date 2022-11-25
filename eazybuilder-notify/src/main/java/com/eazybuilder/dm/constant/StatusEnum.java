package com.eazybuilder.dm.constant;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum StatusEnum {
    requriementToBeSubmited("需求文档待提交"),
    requriementToBeReviewed("需求文档待评审"),
    systemDesignToBeSubmitted("系统设计待提交"),
    systemDesignToBeReviewed("系统设计待评审"),
    developDesignToBeSubmitted("开发设计待提交"),
    developDesignToBeReviewed("开发设计待评审"),
    testDesignToBeSubmitted("测试设计待提交"),
    testDesignToBeReviewed("测试设计待评审"),
    developing("开发中"),
    codeScan("代码扫描中"),
    codeReview("代码审查中"),
    toBeTested("待测试"),
    toBeAccepted("待验收"),
    waitToOnline("待上线"),
    closed("关闭");

    private static final Map<String, StatusEnum> MAP = new HashMap<String, StatusEnum>();

    static {
        for (StatusEnum status : values()) {
            MAP.put(status.name, status);
        }
    }

    private final String name;

    private StatusEnum(String name) {

        this.name = name;
    }

    public static StatusEnum valueOfName(String name) {

        return MAP.get(name);
    }

    public String getName() {
        return name;
    }
}
