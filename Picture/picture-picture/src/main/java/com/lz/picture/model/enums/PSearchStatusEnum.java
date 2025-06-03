package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 搜索状态 枚举
 */
@Getter
public enum PSearchStatusEnum {
    SEARCH_STATUS_0("0", "成功"),
    SEARCH_STATUS_1("1", "失败");

    private static final Map<String, PSearchStatusEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PSearchStatusEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PSearchStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static Optional<PSearchStatusEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
