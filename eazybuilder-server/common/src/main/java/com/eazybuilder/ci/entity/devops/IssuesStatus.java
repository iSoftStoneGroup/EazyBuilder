package com.eazybuilder.ci.entity.devops;

import java.util.HashMap;
import java.util.Map;

public enum IssuesStatus {
    toBeClaimed("待认领"),
    developing("处理中"),
    toBeReviewed("待审核"),
    testing("测试中"),
    toBeOnline("待上线"),
    finished("已上线"),
    rescinded("已撤销"),
    toDeployed("待部署");

    private static final Map<String, IssuesStatus> MAP = new HashMap<String, IssuesStatus>();

    static {
        for (IssuesStatus status : values()) {
            MAP.put(status.name, status);
        }
    }

    private final String name;

    private IssuesStatus(String name) {
        this.name = name;
    }

    public static IssuesStatus valueOfName(String name) {
        return MAP.get(name);
    }

    public String getName() {
        return name;
    }
}
