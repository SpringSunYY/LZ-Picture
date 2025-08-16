package com.lz.points.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 积分使用记录类型 枚举
 * 用于标识积分日志记录的具体类型，例如充值、消费、提成、提现。
 */
@Getter
public enum PoPointsUsageLogTypeEnum {
    /**
     * 充值
     */
    POINTS_USAGE_LOG_TYPE_0("0", "充值"),

    /**
     * 消费
     */
    POINTS_USAGE_LOG_TYPE_1("1", "消费"),

    /**
     * 提成
     */
    POINTS_USAGE_LOG_TYPE_2("2", "提成"),

    /**
     * 提现
     */
    POINTS_USAGE_LOG_TYPE_3("3", "提现"),

    /**
     * 退回
     */
    POINTS_USAGE_LOG_TYPE_4("4", "退回");

    private static final Map<String, PoPointsUsageLogTypeEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PoPointsUsageLogTypeEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PoPointsUsageLogTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据字符串值获取对应的枚举对象
     *
     * @param value 枚举的值
     * @return 对应的枚举对象（Optional 包装）
     */
    public static Optional<PoPointsUsageLogTypeEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
    }
