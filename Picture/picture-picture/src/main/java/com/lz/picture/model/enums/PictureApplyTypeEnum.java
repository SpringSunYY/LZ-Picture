package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 图片申请类型 枚举
 */
@Getter
public enum PictureApplyTypeEnum {

    /**
     * 原创作品
     */
    PICTURE_APPLY_TYPE_0("0", "原创作品"),

    /**
     * 转载资源
     */
    PICTURE_APPLY_TYPE_1("1", "转载资源"),

    /**
     * 无版权资源
     */
    PICTURE_APPLY_TYPE_2("2", "无版权资源");

    private final String value;
    private final String label;

    PictureApplyTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, PictureApplyTypeEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PictureApplyTypeEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    public static Optional<PictureApplyTypeEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
