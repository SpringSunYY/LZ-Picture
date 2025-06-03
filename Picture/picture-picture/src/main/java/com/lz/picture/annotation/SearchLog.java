package com.lz.picture.annotation;

import com.lz.common.enums.OperatorType;

import java.lang.annotation.*;

/**
 * 自定义用户搜索记录注解
 *
 * @author YY
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SearchLog {
    /**
     * 搜索类型
     */
    public String searchType() default "0";

    /**
     * 目标类型
     */
    public String referSource() default "0";
}
