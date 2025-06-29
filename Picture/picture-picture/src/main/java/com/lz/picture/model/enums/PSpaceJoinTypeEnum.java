package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 空间加入类型 枚举
 */
@Getter
public enum PSpaceJoinTypeEnum {

    /** 邀请 */
    SPACE_JOIN_TYPE_1("1", "邀请"),

    /** 创建者 */
    SPACE_JOIN_TYPE_0("0", "创建者");

    private final String value;
    private final String label;

    PSpaceJoinTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, PSpaceJoinTypeEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PSpaceJoinTypeEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    /**
     * 根据值获取枚举对象
     * @param value 字符串值
     * @return 可选枚举对象
     */
    public static Optional<PSpaceJoinTypeEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
