package com.eazybuilder.ci.exception;

public class CIException extends RuntimeException {
    private Integer code;


    public CIException() {
        super();
    }

    public CIException(String message, Throwable cause) {
        super(message, cause);
    }
    public CIException(String message) {
        super(message);
    }

    public CIException(int code,String message){
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
