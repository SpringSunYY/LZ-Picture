package com.lz.common.annotation;

import java.lang.annotation.*;

/**
 * 自定义缓存删除注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomCacheEvict {

    String[] keyPrefixes();

    String[] keyFields() default {}; // 支持嵌套，如 "request.userId"

    boolean useQueryParamsAsKey() default false; // 如果true，加上参数JSON串模糊删除

}
