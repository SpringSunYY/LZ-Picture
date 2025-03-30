package com.lz.config.model.enmus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 菜单显示状态枚举
 */
public enum CMenuVisible {
    MENU_VISIBLE_1("1", "显示"),
    MENU_VISIBLE_2("2", "不显示");

    private static final Map<String, CMenuVisible> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (CMenuVisible item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    CMenuVisible(String value, String label) {
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
    public static Optional<CMenuVisible> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
