package com.lz.ai.strategy.generate.domain.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 使用工厂模式创建参数
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-09  15:30
 * @Version: 1.0
 */
@Data
public abstract class Params<T extends Params<T>> {
    /**
     * 公共参数
     */
    // 宽度
    private int width;
    // 高度
    private int height;
    // 提示词
    private String prompt;

    /**
     * JSON to obj
     */
    public abstract T jsonToObj(String json);

    public abstract String objToJson(T params);
}
