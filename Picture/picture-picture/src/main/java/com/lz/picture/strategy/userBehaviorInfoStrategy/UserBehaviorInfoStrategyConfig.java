package com.lz.picture.strategy.userBehaviorInfoStrategy;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project: Picture
 * Package: com.lz.picture.strategy.userBehaviorInfoStrategy
 * Author: YY
 * CreateTime: 2025-04-14  17:29
 * Description: UserBehaviorInfoStrategyConfig
 * 用户行为策略模式配置类
 * Version: 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface UserBehaviorInfoStrategyConfig {
    String targetType();
    String type();
}
