package com.lz.points.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 账户状态枚举
 */
@Getter
public enum PoAccountStatusEnum {
    ACCOUNT_STATUS_0("0", "正常"),
    ACCOUNT_STATUS_1("1", "异常"),
    ACCOUNT_STATUS_2("2", "禁用");

    private static final Map<String, PoAccountStatusEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PoAccountStatusEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PoAccountStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<PoAccountStatusEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
