package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户行为目标类型 枚举
 */
@Getter
public enum PUserBehaviorTargetTypeEnum {
    USER_BEHAVIOR_TARGET_TYPE_0("0", "图片"),
    USER_BEHAVIOR_TARGET_TYPE_1("1", "空间"),
    USER_BEHAVIOR_TARGET_TYPE_2("2", "用户");

    private static final Map<String, PUserBehaviorTargetTypeEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PUserBehaviorTargetTypeEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PUserBehaviorTargetTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<PUserBehaviorTargetTypeEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
