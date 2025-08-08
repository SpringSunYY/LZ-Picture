package com.lz.ai.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * AI模型参数状态
 */
@Getter
public enum AiModelParamsStatusEnum {

    /**
     * 开启
     */
    MODEL_PARAMS_STATUS_0("0", "开启"),

    /**
     * 关闭
     */
    MODEL_PARAMS_STATUS_1("1", "关闭");

    private final String value;
    private final String label;

    AiModelParamsStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, AiModelParamsStatusEnum> VALUE_MAP = new HashMap<>();

    static {
        for (AiModelParamsStatusEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    public static Optional<AiModelParamsStatusEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
