package com.lz.ai.model.enums;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * AI模型参数类型
 */
@Getter
public enum AiModelParamsTypeEnum {

    /** 文生图 */
    MODEL_PARAMS_TYPE_1("1", "文生图"),

    /** 图生图 */
    MODEL_PARAMS_TYPE_2("2", "图生图");

    private final String value;
    private final String label;

    AiModelParamsTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, AiModelParamsTypeEnum> VALUE_MAP = new HashMap<>();

    static {
        for (AiModelParamsTypeEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    public static Optional<AiModelParamsTypeEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
