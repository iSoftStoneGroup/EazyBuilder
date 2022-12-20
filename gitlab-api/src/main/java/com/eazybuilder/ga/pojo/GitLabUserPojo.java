package com.eazybuilder.ga.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GitLabUserPojo {

    @ApiModelProperty(value = "用户邮箱,不可重复",required=true)
    private String email;
    @ApiModelProperty(value = "用户使用名(需创建用户时填写)",required=false)
    private String username;
    private String password;
    private Boolean admin = true;
    @ApiModelProperty(value = "用户是否需要添加到项目",required=true)
    private Boolean isAddProject;

}
