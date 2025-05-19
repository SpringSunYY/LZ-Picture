package com.lz.points.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 错误处理状态枚举
 */
@Getter
public enum PoErrorResolveStatusEnum {
    ERROR_RESOLVE_STATUS_0("0", "未处理"),
    ERROR_RESOLVE_STATUS_1("1", "处理中"),
    ERROR_RESOLVE_STATUS_2("2", "已解决");

    private static final Map<String, PoErrorResolveStatusEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PoErrorResolveStatusEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PoErrorResolveStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<PoErrorResolveStatusEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
