package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 空间类型枚举
 */
@Getter
public enum PSpaceType {
    SPACE_TYPE_0("0", "官方"),
    SPACE_TYPE_1("1", "团队"),
    SPACE_TYPE_2("2", "个人");

    private static final Map<String, PSpaceType> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PSpaceType item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PSpaceType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<PSpaceType> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
