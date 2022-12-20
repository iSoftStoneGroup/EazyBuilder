package com.eazybuilder.ga.pojo.codeScan;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class CodeReport {

    private Boolean parseFlag;

    private String projectUrl;

    private String link;

    private String branchName;

    private String projectType;

    private String pipelineStatus;

    private List<ScanData> scanDataList;

    private HashMap<String,String> projectAnalysis;

    private List<Stage> stageList;

}
