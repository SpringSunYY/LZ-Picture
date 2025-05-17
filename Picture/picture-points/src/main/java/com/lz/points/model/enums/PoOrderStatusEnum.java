package com.lz.points.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 订单状态枚举
 */
@Getter
public enum PoOrderStatusEnum {
    ORDER_STATUS_0("0", "待支付"),
    ORDER_STATUS_1("1", "支付成功"),
    ORDER_STATUS_2("2", "支付失败"),
    ORDER_STATUS_3("3", "超时"),
    ORDER_STATUS_4("4", "已取消");

    private static final Map<String, PoOrderStatusEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PoOrderStatusEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PoOrderStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<PoOrderStatusEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
