package com.eazybuilder.ci.aspect;

public abstract class DevOpsException extends RuntimeException {
    private Integer code;

    public DevOpsException() {
        super();
    }

    public DevOpsException(String message, Throwable cause) {
        super(message, cause);
    }
    public DevOpsException(String message) {
        super(message);
    }

    public DevOpsException(int code,String message){
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}