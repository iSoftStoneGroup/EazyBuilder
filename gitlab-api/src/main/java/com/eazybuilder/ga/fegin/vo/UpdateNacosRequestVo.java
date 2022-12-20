package com.eazybuilder.ga.fegin.vo;

import java.io.Serializable;

public class UpdateNacosRequestVo implements Serializable {

    private static final long serialVersionUID = -1L;

    private String userName;
    private Integer code;
    private String fileName;
    private byte[]content;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
