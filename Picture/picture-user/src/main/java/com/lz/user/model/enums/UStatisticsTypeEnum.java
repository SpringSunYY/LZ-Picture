package com.lz.user.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户统计类型
 */
@Getter
public enum UStatisticsTypeEnum {

    /**
     * 用户注册
     */
    STATISTICS_TYPE_1("1", "用户注册"),

    /**
     * 用户登录
     */
    STATISTICS_TYPE_2("2", "用户登录"),

    /**
     * 用户IP位置
     */
    STATISTICS_TYPE_3("3", "用户IP位置"),

    /**
     * 用户登录IP位置
     */
    STATISTICS_TYPE_4("4", "用户登录IP位置"),

    /**
     * 用户性别比例
     */
    STATISTICS_TYPE_5("5", "用户性别比例"),

    /**
     * 用户年龄比例
     */
    STATISTICS_TYPE_6("6", "用户年龄比例"),

    /**
     * 消息发送类型
     */
    STATISTICS_TYPE_7("7", "用户消息发送"),
    /**
     * 用户总数
     */
    STATISTICS_TYPE_8("8", "用户总数");

    private final String value;
    private final String label;

    UStatisticsTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, UStatisticsTypeEnum> VALUE_MAP = new HashMap<>();

    static {
        for (UStatisticsTypeEnum item : values()) {
            VALUE_MAP.put(item.value + "_" + item.name(), item);
        }
    }

    /**
     * 根据值获取枚举（注意：如果数据库里有重复值，需要自己额外判断）
     */
    public static Optional<UStatisticsTypeEnum> getEnumByValue(String value) {
        for (UStatisticsTypeEnum item : values()) {
            if (item.value.equals(value)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }
}
