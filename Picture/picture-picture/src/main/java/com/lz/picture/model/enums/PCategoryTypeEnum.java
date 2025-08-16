package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 分类类型
 */
@Getter
public enum PCategoryTypeEnum {

    /**
     * 图片
     */
    CATEGORY_TYPE_0("0", "图片"),

    /**
     * AI生成
     */
    CATEGORY_TYPE_1("1", "AI生成");

    private final String value;
    private final String label;

    PCategoryTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, PCategoryTypeEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PCategoryTypeEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    /**
     * 根据值获取对应的枚举
     */
    public static Optional<PCategoryTypeEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
