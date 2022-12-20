package com.eazybuilder.ga.constant;

import java.util.HashMap;
import java.util.Map;

public enum GuardType {

    bug_blocker("bug_blocker"),
    vulner_blocker("vulner_blocker"),
    code_smell_blocker("code_smell_blocker"),
    unit_test_success_rate("unit_test_success_rate"),
    unit_test_coverage_rate("unit_test_coverage_rate"),
    new_unit_test_coverage_rate("new_unit_test_coverage_rate");

    private static final Map<String, GuardType> MAP = new HashMap<String, GuardType>();

    static {
        for (GuardType guardType : values()) {
            MAP.put(guardType.name, guardType);
        }
    }

    private final String name;

    private GuardType(String name) {

        this.name = name;
    }

    public static GuardType valueOfName(String name) {

        return MAP.get(name);
    }

    public String getName() {
        return name;
    }

}
