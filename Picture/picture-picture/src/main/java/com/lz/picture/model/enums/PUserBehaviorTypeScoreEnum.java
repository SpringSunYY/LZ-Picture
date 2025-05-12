package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户行为类型 枚举
 */
@Getter
public enum PUserBehaviorTypeScoreEnum {
    USER_BEHAVIOR_TYPE_SCORE_0("0", 3.0, "点赞"),
    USER_BEHAVIOR_TYPE_SCORE_1("1", 6.0, "收藏"),
    USER_BEHAVIOR_TYPE_SCORE_2("2", 12.0, "转发");

    private static final Map<String, PUserBehaviorTypeScoreEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PUserBehaviorTypeScoreEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final Double score;
    private final String label;

    PUserBehaviorTypeScoreEnum(String value, Double score, String label) {
        this.value = value;
        this.score = score;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<PUserBehaviorTypeScoreEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
