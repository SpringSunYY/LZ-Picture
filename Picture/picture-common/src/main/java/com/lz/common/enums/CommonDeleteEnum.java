package com.lz.common.enums;

import com.lz.common.utils.StringUtils;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project: config
 * Description: CommonDeleteEnum
 * 通用删除状态枚举（支持状态标签样式）
 * Version: 1.0
 */
public enum CommonDeleteEnum {

    NORMAL("正常", "0"),
    DELETED("删除", "1");

    private final String text;
    private final String value;
    private static final Map<String, CommonDeleteEnum> VALUE_MAP = new ConcurrentHashMap<>();

    static {
        for (CommonDeleteEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    CommonDeleteEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    // 状态样式映射（根据第六列参数）
    public String getStyleType() {
        return switch (value) {
            case "0" -> "primary";
            case "1" -> "warning";
            default -> "default";
        };
    }

    public static Optional<CommonDeleteEnum> getByValue(String value) {
        return StringUtils.isEmpty(value) ? 
               Optional.empty() : 
               Optional.ofNullable(VALUE_MAP.get(value));
    }

    // 新增逻辑删除校验方法
    public boolean isDeleted() {
        return this == DELETED;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}