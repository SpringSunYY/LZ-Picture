package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 浏览记录目标类型 枚举
 */
@Getter
public enum PViewLogTargetTypeEnum {
    VIEW_LOG_TARGET_TYPE_0("0", "图片"),
    VIEW_LOG_TARGET_TYPE_1("1", "空间"),
    VIEW_LOG_TARGET_TYPE_2("2", "用户");

    private static final Map<String, PViewLogTargetTypeEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PViewLogTargetTypeEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PViewLogTargetTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static Optional<PViewLogTargetTypeEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
