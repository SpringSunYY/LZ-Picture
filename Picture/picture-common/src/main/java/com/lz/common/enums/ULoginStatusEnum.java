package com.lz.common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 登录状态 枚举
 */
@Getter
public enum ULoginStatusEnum {
    LOGIN_STATUS_0("0", "成功"),
    LOGIN_STATUS_1("1", "失败");

    private static final Map<String, ULoginStatusEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (ULoginStatusEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    ULoginStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<ULoginStatusEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
