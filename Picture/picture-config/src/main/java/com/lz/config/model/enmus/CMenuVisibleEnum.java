package com.lz.config.model.enmus;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 菜单显示状态枚举
 */
@Getter
public enum CMenuVisibleEnum {
    MENU_VISIBLE_0("0", "显示"),
    MENU_VISIBLE_1("1", "不显示");

    private static final Map<String, CMenuVisibleEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (CMenuVisibleEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    CMenuVisibleEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<CMenuVisibleEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
