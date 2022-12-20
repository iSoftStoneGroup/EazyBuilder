package com.eazybuilder.ga.pojo.rule;

import lombok.Data;

@Data
public class GuardPojo {

    private String guardType;

    private Integer thresholdMin;

    private Integer thresholdMax;

}
