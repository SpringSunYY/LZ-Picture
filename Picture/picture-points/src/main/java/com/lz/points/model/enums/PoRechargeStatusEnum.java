package com.lz.points.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 充值状态枚举
 */
@Getter
public enum PoRechargeStatusEnum {
    RECHARGE_STATUS_0("0", "待支付"),
    RECHARGE_STATUS_1("1", "支付成功"),
    RECHARGE_STATUS_2("2", "支付失败"),
    RECHARGE_STATUS_3("3", "超时"),
    RECHARGE_STATUS_4("4", "已取消");

    private static final Map<String, PoRechargeStatusEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PoRechargeStatusEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PoRechargeStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<PoRechargeStatusEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
