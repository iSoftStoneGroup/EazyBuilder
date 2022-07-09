package com.eazybuilder.ci.entity.devops;


import java.io.Serializable;
import java.util.Date;

public class RedmineProject implements Serializable {

    private static final long serialVersionUID = -1L;
    //@ApiModelProperty(value = "项目ID")
    private Integer id;
    //@ApiModelProperty(value = "创建时间")
    private Date createOn;
    //@ApiModelProperty(value = "描述")
    private String description;
    //@ApiModelProperty(value = "项目主页")
    private String homePage;
    //@ApiModelProperty(value = "项目唯一标识")
    private String identifier;
    //@ApiModelProperty(value = "是否集成父项目成员")
    private Boolean inheritMembers;
    //@ApiModelProperty(value = "项目名称")
    private String name;
    //@ApiModelProperty(value = "父项目ID")
    private Integer parentId;
    //@ApiModelProperty(value = "项目状态")
    private Integer status;
    //@ApiModelProperty(value = "更新时间")
    private Date updateOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Boolean getInheritMembers() {
        return inheritMembers;
    }

    public void setInheritMembers(Boolean inheritMembers) {
        this.inheritMembers = inheritMembers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdateOn() {
        return updateOn;
    }

    public void setUpdateOn(Date updateOn) {
        this.updateOn = updateOn;
    }
}
