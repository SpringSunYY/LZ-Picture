package com.lz.points.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 积分/充值统计类型 枚举
 */
@Getter
public enum PoStatisticsTypeEnum {

    STATISTICS_TYPE_1("1", "积分使用类型"),
    STATISTICS_TYPE_2("2", "用户充值排行"),
    STATISTICS_TYPE_3("3", "积分使用"),
    STATISTICS_TYPE_4("4", "充值金额"),
    STATISTICS_TYPE_5("5", "充值订单"),
    STATISTICS_TYPE_6("6", "套餐充值排行"),
    STATISTICS_TYPE_7("7", "充值方式");

    private static final Map<String, PoStatisticsTypeEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PoStatisticsTypeEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PoStatisticsTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<PoStatisticsTypeEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
