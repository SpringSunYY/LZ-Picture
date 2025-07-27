package com.lz.common.annotation;

import java.lang.annotation.*;

/**
 * 自定义排序注解
 * 首先我希望我变好，也希望你
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-07-27  16:41
 * @Version: 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomSort {
    String[] sortFields() default {};   //排序字段
    String[] sortMappingFields() default {}; //映射字段，前端只需要传过来字段名，如果多表查询设置映射字段，映射字段为查询时所需字段
}
