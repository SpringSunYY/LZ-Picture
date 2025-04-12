package com.lz.picture.annotation;

import com.lz.common.enums.BusinessType;
import com.lz.common.enums.OperatorType;

import java.lang.annotation.*;
import java.math.BigDecimal;

/**
 * 自定义图片浏览记录注解
 *
 * @author YY
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserViewLog {
    /**
     * 分数
     */
    public double score() default 1;

    /**
     * 目标类型
     */
    public String targetType() default "0";

    /**
     * 操作人类别
     */
    public OperatorType operatorType() default OperatorType.MANAGE;
}
