package com.eazybuilder.ga.pojo.codeScan;

import lombok.Data;

@Data
public class ScanData {

    private String type;

    private Integer blockerCount;

    private Integer criticalCount;

    private Integer majorCount;

    private Integer minorCount;

    private Integer infoCount;

}
