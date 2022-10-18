package com.eazybuilder.ci.aspect;

import java.lang.annotation.*;

/**
 * 异常状态信息
 * 用于自定义异常的定义
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionStatus {

    /**
     * @return 状态
     */
    ResStatus status() default ResStatus.UNKOWN;

    /**
     * @return 状态描述
     */
    String description() default "";


}
