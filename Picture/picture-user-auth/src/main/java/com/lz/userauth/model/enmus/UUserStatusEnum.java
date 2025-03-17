package com.lz.userauth.model.enmus;

import com.lz.common.utils.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project: user
 * Description: UUserStatusEnum
 * 用户状态枚举（含状态样式类型）
 * Version: 1.0
 */
public enum UUserStatusEnum {

    USER_STATUS_0("正常", "0"),
    USER_STATUS_1("异常", "1"),
    USER_STATUS_2("禁用", "2");

    private final String text; // 状态描述
    private final String value; // 状态值

    private static final Map<String, UUserStatusEnum> VALUE_TO_ENUM = new ConcurrentHashMap<>();

    static {
        for (UUserStatusEnum enumValue : UUserStatusEnum.values()) {
            VALUE_TO_ENUM.put(enumValue.value, enumValue);
        }
    }

    UUserStatusEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static Optional<UUserStatusEnum> getEnumByValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }

    // 新增状态样式类型映射（根据数据第六列）
    public String getStatusStyle() {
        return switch (value) {
            case "0" -> "primary";
            case "1" -> "warning";
            case "2" -> "danger";
            default -> "default";
        };
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}