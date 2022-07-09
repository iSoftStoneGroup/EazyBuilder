package com.eazybuilder.ci.config;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.persistence.GenerationType;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static javax.persistence.GenerationType.IDENTITY;

@Target({ METHOD, FIELD })
@Retention(RUNTIME)

public @interface GeneratedValue {

	/**
	 * 
	 * 默认使用mysql数据库的自增
	 */
	GenerationType strategy() default IDENTITY;

	String generator() default "";
}
