package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 分类是否支持查询状态 枚举
 */
@Getter
public enum PCategoryQueryStatusEnum {

    /** 是 */
    CATEGORY_QUERY_STATUS_0("0", "是"),

    /** 否 */
    CATEGORY_QUERY_STATUS_1("1", "否");

    private final String value;
    private final String label;

    PCategoryQueryStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, PCategoryQueryStatusEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PCategoryQueryStatusEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    public static Optional<PCategoryQueryStatusEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
