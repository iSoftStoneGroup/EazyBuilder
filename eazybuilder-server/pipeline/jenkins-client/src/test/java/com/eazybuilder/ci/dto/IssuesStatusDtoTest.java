package com.eazybuilder.ci.dto;

import org.junit.Assert;

public class IssuesStatusDtoTest {
    public void issuesStatusDtoTest(){
        IssuesStatusDto issuesStatusDto = new IssuesStatusDto();


        issuesStatusDto.setIssuesStatus("");
        issuesStatusDto.setUserName("");
        issuesStatusDto.setCode("");

        Assert.assertNotNull(issuesStatusDto.getIssuesStatus());
        Assert.assertNotNull(issuesStatusDto.getUserName());
        Assert.assertNotNull(issuesStatusDto.getCode());
    }
}
