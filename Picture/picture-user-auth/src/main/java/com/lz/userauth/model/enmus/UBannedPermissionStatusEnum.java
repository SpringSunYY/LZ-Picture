package com.lz.userauth.model.enmus;

import com.lz.common.utils.StringUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Project: user
 * Description: UBannedPermissionStatusEnum
 * 用户封禁权限状态枚举
 * Version: 1.0
 */
public enum UBannedPermissionStatusEnum {

    BANNED_PERMISSION_STATUS_0("封禁中", "0"),
    BANNED_PERMISSION_STATUS_1("结束", "1");

    private final String text; // 状态描述
    private final String value; // 状态值

    private static final Map<String, UBannedPermissionStatusEnum> VALUE_TO_ENUM = new ConcurrentHashMap<>();

    static {
        for (UBannedPermissionStatusEnum enumValue : values()) {
            VALUE_TO_ENUM.put(enumValue.value, enumValue);
        }
    }

    UBannedPermissionStatusEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    // 新增状态样式映射（根据第六列参数）
    public String getStatusStyle() {
        return switch (value) {
            case "0" -> "danger";
            case "1" -> "warning";
            default -> "default";
        };
    }

    public static Optional<UBannedPermissionStatusEnum> getEnumByValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }

    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(UBannedPermissionStatusEnum::getValue)
                .collect(Collectors.toList());
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}