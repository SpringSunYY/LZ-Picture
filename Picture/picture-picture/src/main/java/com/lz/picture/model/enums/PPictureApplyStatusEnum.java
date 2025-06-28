package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 图片申请状态 枚举
 */
@Getter
public enum PPictureApplyStatusEnum {

    /**
     * 待审核
     */
    PICTURE_APPLY_STATUS_0("0", "待审核"),

    /**
     * 同意
     */
    PICTURE_APPLY_STATUS_1("1", "同意"),

    /**
     * 拒绝
     */
    PICTURE_APPLY_STATUS_2("2", "拒绝");

    private final String value;
    private final String label;

    PPictureApplyStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, PPictureApplyStatusEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PPictureApplyStatusEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    public static Optional<PPictureApplyStatusEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
