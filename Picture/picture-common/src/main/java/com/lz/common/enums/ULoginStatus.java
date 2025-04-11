package com.lz.common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 登录状态 枚举
 */
@Getter
public enum ULoginStatus {
    LOGIN_STATUS_0("0", "成功"),
    LOGIN_STATUS_1("1", "失败");

    private static final Map<String, ULoginStatus> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (ULoginStatus item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    ULoginStatus(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<ULoginStatus> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
