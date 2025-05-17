package com.lz.points.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 支付方式枚举
 */
@Getter
public enum PoPaymentTypeEnum {
    PAYMENT_TYPE_0("0", "支付宝"),
    PAYMENT_TYPE_1("1", "微信");

    private static final Map<String, PoPaymentTypeEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PoPaymentTypeEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PoPaymentTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<PoPaymentTypeEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
