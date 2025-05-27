package com.lz.user.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户通知是否已读枚举
 */
@Getter
public enum UInformIsReadEnum {
    INFORM_IS_READ_0("0", "未读"),
    INFORM_IS_READ_1("1", "已读");

    private static final Map<String, UInformIsReadEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (UInformIsReadEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    UInformIsReadEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static Optional<UInformIsReadEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) return Optional.empty();
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
