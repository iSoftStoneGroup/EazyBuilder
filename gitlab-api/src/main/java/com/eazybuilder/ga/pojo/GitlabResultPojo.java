package com.eazybuilder.ga.pojo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class GitlabResultPojo {

    private Boolean successFlag;

    private String resultMessage;

    private JSONObject resultJson;

}
