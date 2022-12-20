package com.eazybuilder.ga.pojo;

import com.eazybuilder.ga.constant.ProfileType;
import lombok.Data;

import java.util.HashMap;

@Data
public class CIPackagePojo {

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

    private String authorName;

    private String assigneeName;

    private ProfileType profileType;

    private HashMap<String,String> customFields;

}
