package com.eazybuilder.ci.controller.vo;

import com.eazybuilder.ci.entity.Project;


public class PipelineBuildVo extends Project {

    private String redmineCode;
    private String redmineUser;

    public String getRedmineCode() {
        return redmineCode;
    }

    public void setRedmineCode(String redmineCode) {
        this.redmineCode = redmineCode;
    }

    public String getRedmineUser() {
        return redmineUser;
    }

    public void setRedmineUser(String redmineUser) {
        this.redmineUser = redmineUser;
    }
}
