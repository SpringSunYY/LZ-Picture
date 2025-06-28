package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 空间扩容类型 枚举
 */
@Getter
public enum PSpaceDilatationTypeEnum {

    /** 容量扩容 */
    SPACE_DILATATION_TYPE_0("0", "容量扩容"),

    /** 数量扩容 */
    SPACE_DILATATION_TYPE_1("1", "数量扩容"),

    /** 人数扩容 */
    SPACE_DILATATION_TYPE_2("2", "人数扩容");

    private final String value;
    private final String label;

    PSpaceDilatationTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, PSpaceDilatationTypeEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PSpaceDilatationTypeEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    /**
     * 根据值获取枚举对象
     * @param value 字符串值
     * @return 可选枚举对象
     */
    public static Optional<PSpaceDilatationTypeEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
