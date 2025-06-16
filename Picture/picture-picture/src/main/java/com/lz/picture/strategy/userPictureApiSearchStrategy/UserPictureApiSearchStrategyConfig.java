package com.lz.picture.strategy.userPictureApiSearchStrategy;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户图片api搜索策略配置
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-16  14:54
 * @Version: 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface UserPictureApiSearchStrategyConfig {
    /**
     * 根据api选择对应的api
     */
    String api();
}
