package com.eazybuilder.ga.constant;

import java.util.HashMap;
import java.util.Map;

public enum ProjectType {
    java("java"),
    npm("npm"),
    gradle("gradle"),
    all("all"),
    initDeploy("initDeploy"),
    dataBaseScript("dataBaseScript"),
    net("net");

    private static final Map<String, ProjectType> MAP = new HashMap<String, ProjectType>();

    static {
        for (ProjectType status : values()) {
            MAP.put(status.name, status);
        }
    }

    private final String name;

    private ProjectType(String name) {

        this.name = name;
    }

    public static ProjectType valueOfName(String name) {

        return MAP.get(name);
    }

    public String getName() {
        return name;
    }
}
