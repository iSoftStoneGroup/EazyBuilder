package com.eazybuilder.dm.base;

import com.querydsl.core.types.dsl.EntityPathBase;

import java.lang.annotation.*;

/**
 * 与PageSearchDsl配合使用，获取注解中的信息以构建分页条件查询所需的predicate
 * {@link com.eazybuilder.tcm.base.AbstractCommonServiceImpl}实现了{@link com.eazybuilder.tcm.base.PageSearchDsl}接口
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface PageSearch {

    /**
     * QueryDsl的Q类
     * @return
     */
    Class<? extends EntityPathBase> value() ;

    /**
     * 需要查询的字段名
     * TODO 暂时不支持对象符（.）导航
     * @return
     */
    String [] fields() default {};

}
