package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 分类状态 枚举
 */
@Getter
public enum PCategoryStatusEnum {

    /** 正常 */
    CATEGORY_STATUS_0("0", "正常"),

    /** 关闭 */
    CATEGORY_STATUS_1("1", "关闭");

    private final String value;
    private final String label;

    PCategoryStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, PCategoryStatusEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PCategoryStatusEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    public static Optional<PCategoryStatusEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
