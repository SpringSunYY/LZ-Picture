package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户行为类型 枚举
 */
@Getter
public enum PUserBehaviorType {
    USER_BEHAVIOR_TYPE_0("0", "点赞"),
    USER_BEHAVIOR_TYPE_1("1", "收藏"),
    USER_BEHAVIOR_TYPE_2("2", "转发");

    private static final Map<String, PUserBehaviorType> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PUserBehaviorType item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PUserBehaviorType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<PUserBehaviorType> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
