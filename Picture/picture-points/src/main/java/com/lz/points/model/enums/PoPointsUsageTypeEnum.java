package com.lz.points.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 积分用途类型 枚举
 * 表示积分被使用的具体业务类型，例如下载图片。
 */
@Getter
public enum PoPointsUsageTypeEnum {
    /**
     * 下载图片
     */
    POINTS_USAGE_TYPE_0("0", "下载图片"),
    /**
     * 空间扩容
     */
    POINTS_USAGE_TYPE_1("1", "空间扩容");

    private static final Map<String, PoPointsUsageTypeEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PoPointsUsageTypeEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PoPointsUsageTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据字符串值获取对应的枚举对象
     *
     * @param value 枚举的值
     * @return 对应的枚举对象（Optional 包装）
     */
    public static Optional<PoPointsUsageTypeEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
