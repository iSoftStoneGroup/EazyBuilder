package com.eazybuilder.ga.constant;

import java.util.HashMap;
import java.util.Map;

public enum StatusEnum {
    toBeClaimed("待认领"),
    developing("处理中"),
    toBeReviewed("待审核"),
    testFailed("测试打回"),
    testing("测试中"),
    toBeOnline("待上线"),
    toDeployed("待部署"),
    finished("已上线"),
    rescinded("已撤销");

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
