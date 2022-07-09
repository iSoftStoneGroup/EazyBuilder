package com.eazybuilder.ci.entity.pipeline;

import com.eazybuilder.ci.constant.ProfileType;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="CI_PACKAGE")
public class CIPackage {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id",columnDefinition="int(8)")
    private String id;

    private Date createDate;

    private String projectName;

    private String profileName;

    private String profileCode;

    private String userName;

    private String imageTag;

    private String sourceBranchName;

    private String targetBranchName;

    private String code;

    private String topCode;

    private String tagName;

    private String gitPath;

    private String profileType;

    @ElementCollection
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="ci_package_fields", joinColumns=@JoinColumn(name="ci_package_id"))
    private Map<String,String> customFields = new HashMap<String, String>();

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageTag() {
        return imageTag;
    }

    public void setImageTag(String imageTag) {
        this.imageTag = imageTag;
    }

    public String getSourceBranchName() {
        return sourceBranchName;
    }

    public void setSourceBranchName(String sourceBranchName) {
        this.sourceBranchName = sourceBranchName;
    }

    public String getTargetBranchName() {
        return targetBranchName;
    }

    public void setTargetBranchName(String targetBranchName) {
        this.targetBranchName = targetBranchName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTopCode() {
        return topCode;
    }

    public void setTopCode(String topCode) {
        this.topCode = topCode;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getGitPath() {
        return gitPath;
    }

    public void setGitPath(String gitPath) {
        this.gitPath = gitPath;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public Map<String, String> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Map<String, String> customFields) {
        this.customFields = customFields;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
