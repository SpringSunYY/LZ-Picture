package com.lz.config.model.enmus;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 配置是否启用枚举
 */
@Getter
public enum CConfigIsInEnum {
    CONFIG_IS_IN_0("0", "是"),
    CONFIG_IS_IN_1("1", "否");

    private static final Map<String, CConfigIsInEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (CConfigIsInEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    CConfigIsInEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<CConfigIsInEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
