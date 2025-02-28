package com.lz.config.model.enmus;

import com.lz.common.utils.StringUtils;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Project: config
 * Description: CPermissionStatusEnum
 * 权限状态枚举
 * Version: 1.0
 */
@Getter
public enum CPermissionStatusEnum {

    PERMISSION_STATUS_0("正常", "0"),
    PERMISSION_STATUS_1("隐藏", "1");

    private final String text; // 字典描述（label）
    private final String value; // 字典值

    private static final Map<String, CPermissionStatusEnum> VALUE_TO_ENUM = new ConcurrentHashMap<>();

    static {
        for (CPermissionStatusEnum enumValue : CPermissionStatusEnum.values()) {
            VALUE_TO_ENUM.put(enumValue.value, enumValue);
        }
    }

    CPermissionStatusEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(CPermissionStatusEnum::getValue)
                .collect(Collectors.toList());
    }

    public static Optional<CPermissionStatusEnum> getEnumByValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }

}