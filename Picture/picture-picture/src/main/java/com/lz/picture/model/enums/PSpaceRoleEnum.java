package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 空间角色类型 枚举
 */
@Getter
public enum PSpaceRoleEnum {

    /**
     * 创建者
     */
    SPACE_ROLE_0("0", "创建者"),

    /**
     * 管理员
     */
    SPACE_ROLE_1("1", "管理员"),

    /**
     * 编辑者
     */
    SPACE_ROLE_2("2", "编辑者"),

    /**
     * 预览者
     */
    SPACE_ROLE_3("3", "预览者");

    private final String value;
    private final String label;

    PSpaceRoleEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, PSpaceRoleEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PSpaceRoleEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    /**
     * 根据值获取枚举对象
     *
     * @param value 字符串值
     * @return 可选枚举对象
     */
    public static Optional<PSpaceRoleEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
