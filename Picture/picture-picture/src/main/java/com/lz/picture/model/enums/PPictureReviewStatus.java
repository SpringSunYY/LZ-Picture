package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 图片审核状态 枚举
 */
@Getter
public enum PPictureReviewStatus {
    PICTURE_REVIEW_STATUS_0("0", "待审核"),
    PICTURE_REVIEW_STATUS_1("1", "同意"),
    PICTURE_REVIEW_STATUS_2("2", "拒绝");

    private static final Map<String, PPictureReviewStatus> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PPictureReviewStatus item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PPictureReviewStatus(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<PPictureReviewStatus> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
