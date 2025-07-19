package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 统计类型 枚举
 */
@Getter
public enum PStatisticsTypeEnum {

    /** 五分钟 */
    STATISTICS_TYPE_1("1", "五分钟"),

    /** 日 */
    STATISTICS_TYPE_2("2", "日"),

    /** 月 */
    STATISTICS_TYPE_3("3", "月"),

    /** 年 */
    STATISTICS_TYPE_4("4", "年");

    private final String value;
    private final String label;

    PStatisticsTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, PStatisticsTypeEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PStatisticsTypeEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    /**
     * 根据值获取枚举对象
     * @param value 枚举值
     * @return 可选枚举对象
     */
    public static Optional<PStatisticsTypeEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
