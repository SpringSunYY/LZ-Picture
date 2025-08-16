package com.lz.ai.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * AI生成是否发布
 */
@Getter
public enum AiGenerateHasPublicEnum {

    /**
     * 已发布
     */
    HAS_PUBLIC_0("0", "已发布"),

    /**
     * 未发布
     */
    HAS_PUBLIC_1("1", "未发布");

    private final String value;
    private final String label;

    AiGenerateHasPublicEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, AiGenerateHasPublicEnum> VALUE_MAP = new HashMap<>();

    static {
        for (AiGenerateHasPublicEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    /**
     * 根据值获取对应的枚举
     */
    public static Optional<AiGenerateHasPublicEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
