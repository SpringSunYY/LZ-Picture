package com.lz.points.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 异常日志类型枚举
 */
@Getter
public enum PoErrorLogTypeEnum {
    ERROR_LOG_TYPE_1("1", "支付失败"),
    ERROR_LOG_TYPE_2("2", "签名失效");

    private static final Map<String, PoErrorLogTypeEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PoErrorLogTypeEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PoErrorLogTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<PoErrorLogTypeEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
