package com.eazybuilder.ci.aspect;

public enum ResStatus {
    /**
     * 未登录
     */
    NEVER_LOGIN(600, "未登录"),
    /**
     * 状态未知
     */
    UNKOWN(900, "未知错误");

    private int status;
    private String description;
    private ResStatus(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
