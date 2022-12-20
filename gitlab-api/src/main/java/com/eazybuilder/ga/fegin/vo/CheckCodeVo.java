package com.eazybuilder.ga.fegin.vo;

import java.io.Serializable;

public class CheckCodeVo implements Serializable {

    private static final long serialVersionUID = -1L;

    private Boolean flag;

    private String message;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
