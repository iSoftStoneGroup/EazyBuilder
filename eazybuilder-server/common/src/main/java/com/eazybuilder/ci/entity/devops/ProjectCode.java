package com.eazybuilder.ci.entity.devops;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ProjectCode {

    dtp,
    redmine,
    /**
     * 发版
     */
    notify,

    gitlab,

    harbor;


}
