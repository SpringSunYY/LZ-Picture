package com.lz.ai.strategy.generate;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AI生成策略模式执行器配置
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-08  23:47
 * @Version: 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface AiGenerateStrategyConfig {
    String model();
}
