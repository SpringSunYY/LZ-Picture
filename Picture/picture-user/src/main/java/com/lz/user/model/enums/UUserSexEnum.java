package com.lz.user.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户性别枚举
 */
@Getter
public enum UUserSexEnum {
    USER_SEX_0("0", "未知"),
    USER_SEX_1("1", "男"),
    USER_SEX_2("2", "女");

    private static final Map<String, UUserSexEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (UUserSexEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    UUserSexEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<UUserSexEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
