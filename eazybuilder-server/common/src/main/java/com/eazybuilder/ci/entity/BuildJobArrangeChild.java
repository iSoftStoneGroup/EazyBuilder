package com.eazybuilder.ci.entity;

import com.eazybuilder.ci.constant.PreTaskStatus;

/**
 * @author: sanzhang
 * @createTime: 2022/8/17
 * @description:
 **/
public class BuildJobArrangeChild {

    private String buildJobId;

    private String buildJobName;


    public String getBuildJobId() {
        return buildJobId;
    }

    public void setBuildJobId(String buildJobId) {
        this.buildJobId = buildJobId;
    }

    public String getBuildJobName() {
        return buildJobName;
    }

    public void setBuildJobName(String buildJobName) {
        this.buildJobName = buildJobName;
    }
}
