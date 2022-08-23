package com.eazybuilder.ci.aspect;

import com.eazybuilder.ci.exception.CIException;

/**
 * 未登录异常
 *
 */
@ExceptionStatus(status = ResStatus.NEVER_LOGIN, description = "未登录")
public class NeverLoginException extends CIException {
    private static final long serialVersionUID = 3892919274244039652L;

    public NeverLoginException(String message) {
        super(message);
    }

}