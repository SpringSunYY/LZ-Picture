package com.lz.user.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户通知状态枚举
 */
@Getter
public enum UInformStatusEnum {
    INFORM_STATUS_0("0", "待发送"),
    INFORM_STATUS_1("1", "已发送"),
    INFORM_STATUS_2("2", "发送失败"),
    INFORM_STATUS_3("3", "撤回");

    private static final Map<String, UInformStatusEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (UInformStatusEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    UInformStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static Optional<UInformStatusEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) return Optional.empty();
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
