package com.lz.common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 是否统计 枚举
 */
@Getter
public enum CommonHasStatisticsEnum {
    /**
     * 否
     */
    HAS_STATISTICS_0("0", "否"),

    /**
     * 是
     */
    HAS_STATISTICS_1("1", "是");

    private static final Map<String, CommonHasStatisticsEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (CommonHasStatisticsEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    CommonHasStatisticsEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<CommonHasStatisticsEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
