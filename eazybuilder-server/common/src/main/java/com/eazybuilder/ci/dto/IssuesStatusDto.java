package com.eazybuilder.ci.dto;

import com.eazybuilder.ci.entity.devops.IssuesStatus;


public class IssuesStatusDto {

    //需求状态
    private String issuesStatus;
    //修改人
    private String userName;
    //需求号
    private String code;

    public String getIssuesStatus() {
        return issuesStatus;
    }

    public void setIssuesStatus(String issuesStatus) {
        this.issuesStatus = issuesStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
