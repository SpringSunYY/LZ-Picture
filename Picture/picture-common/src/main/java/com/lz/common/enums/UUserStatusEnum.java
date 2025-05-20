package com.lz.common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户状态枚举
 */
@Getter
public enum UUserStatusEnum {
    USER_STATUS_0("0", "正常"),
    USER_STATUS_1("1", "异常"),
    USER_STATUS_2("2", "禁用");

    private static final Map<String, UUserStatusEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (UUserStatusEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    UUserStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<UUserStatusEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
