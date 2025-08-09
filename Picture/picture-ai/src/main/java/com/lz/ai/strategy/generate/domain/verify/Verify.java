package com.lz.ai.strategy.generate.domain.verify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参数校验模板
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-09  16:10
 * @Version: 1.0
 */
@Data
public abstract class Verify<T extends Verify<T>> {
    //宽度 0小 1大，存两个
    private Integer maxWidth;
    //高度 0小 1大，存两个
    private Integer minWidth;
    private Integer maxHeight;
    private Integer minHeight;
    //promptLength
    private Integer promptLength;

    /**
     * JSON to obj
     */
    public abstract String objToJson(T verify);

    public abstract T jsonToObj(String json);
}
