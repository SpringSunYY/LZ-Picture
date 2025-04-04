package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 空间角色枚举
 */
@Getter
public enum PSpaceRole {
    SPACE_ROLE_0("0", "创建者"),
    SPACE_ROLE_1("1", "管理员"),
    SPACE_ROLE_2("2", "编辑者"),
    SPACE_ROLE_3("3", "预览者");

    private static final Map<String, PSpaceRole> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PSpaceRole item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PSpaceRole(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<PSpaceRole> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
