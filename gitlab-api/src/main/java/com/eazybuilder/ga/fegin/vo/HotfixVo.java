package com.eazybuilder.ga.fegin.vo;

import java.io.Serializable;

public class HotfixVo implements Serializable {

    private static final long serialVersionUID = -1L;

    private String code;

    private String description;

    private String subject;

    private String assigneeName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }
}
