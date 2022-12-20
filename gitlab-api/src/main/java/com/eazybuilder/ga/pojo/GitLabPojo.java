package com.eazybuilder.ga.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class GitLabPojo {


    @ApiModelProperty(value = "组路径",required=true)
    private String groupPath;

    @ApiModelProperty(value = "项目名",required=true)
    private List<String> projectList;

    @ApiModelProperty(value = "用户信息",required=true)
    private List<GitLabUserPojo> groupAddUserEmails;







}
