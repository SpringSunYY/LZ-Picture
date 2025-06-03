package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 搜索类型 枚举
 */
@Getter
public enum PSearchTypeEnum {
    SEARCH_TYPE_0("0", "图片"),
    SEARCH_TYPE_1("1", "空间"),
    SEARCH_TYPE_2("2", "用户"),
    SEARCH_TYPE_4("4", "历史搜索");

    private static final Map<String, PSearchTypeEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PSearchTypeEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PSearchTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static Optional<PSearchTypeEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
