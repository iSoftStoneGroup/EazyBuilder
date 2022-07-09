package com.eazybuilder.ci.auth.methodAuthentication;

import com.eazybuilder.ci.constant.RoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * 当前用户必须包含roleEnums定义的每一个角色枚举信息才可以访问
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HaveRole {
    RoleEnum[] roleEnums() default {};

}