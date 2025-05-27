package com.lz.user.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户通知类型枚举
 */
@Getter
public enum UInformTypeEnum {
    INFORM_TYPE_0("0", "通知"),
    INFORM_TYPE_1("1", "公告");

    private static final Map<String, UInformTypeEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (UInformTypeEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    UInformTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static Optional<UInformTypeEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) return Optional.empty();
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
