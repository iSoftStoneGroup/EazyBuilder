package com.eazybuilder.ga.pojo.codeScan;

import lombok.Data;

@Data
public class Stage {

    private String id;
    private String name;
    private String status;
    private long startTimeMillis;
    private long durationMillis;

}
