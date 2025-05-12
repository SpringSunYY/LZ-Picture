package com.lz.config.model.enmus;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 菜单类型枚举
 */
@Getter
public enum CMenuTypeEnum {
    MENU_TYPE_M("M", "目录"),
    MENU_TYPE_C("C", "菜单"),
    MENU_TYPE_B("B", "按钮"),
    MENU_TYPE_F("F", "功能"),
    MENU_TYPE_T("T", "Tabs");

    private static final Map<String, CMenuTypeEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (CMenuTypeEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    CMenuTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<CMenuTypeEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
