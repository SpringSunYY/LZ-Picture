package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 搜索来源 枚举
 */
@Getter
public enum PSearchReferSourceEnum {
    SEARCH_REFER_SOURCE_0("0", "首页"),
    SEARCH_REFER_SOURCE_1("1", "推荐"),
    SEARCH_REFER_SOURCE_2("2", "搜索页"),
    SEARCH_REFER_SOURCE_3("3", "AI推荐");

    private static final Map<String, PSearchReferSourceEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PSearchReferSourceEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PSearchReferSourceEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static Optional<PSearchReferSourceEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
