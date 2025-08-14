package com.lz.ai.model.enums;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * AI提示状态
 */
@Getter
public enum AIPromptStatusEnum {

    /** 使用 */
    PROMPT_STATUS_0("0", "使用"),

    /** 未使用 */
    PROMPT_STATUS_1("1", "未使用");

    private final String value;
    private final String label;

    AIPromptStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, AIPromptStatusEnum> VALUE_MAP = new HashMap<>();

    static {
        for (AIPromptStatusEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    public static Optional<AIPromptStatusEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
