package com.eazybuilder.dm.constant;

import java.util.HashMap;
import java.util.Map;

public enum RoleEnum {

    demander("需求"),
    tester("测试"),
    developer("开发"),
    implementer("实施"),
    review("评审");

    private static final Map<String, RoleEnum> MAP = new HashMap<String, RoleEnum>();

    static {
        for (RoleEnum role : values()) {
            MAP.put(role.name, role);
        }
    }

    private final String name;

    private RoleEnum(String name) {

        this.name = name;
    }

    public static RoleEnum valueOfName(String name) {

        return MAP.get(name);
    }

}
