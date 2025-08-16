package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 图片申请类型 枚举
 */
@Getter
public enum PPictureApplyTypeEnum {

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
    PICTURE_APPLY_TYPE_2("2", "无版权资源"),

    /**
     * AI生成
     */
    PICTURE_APPLY_TYPE_3("3", "AI生成");

    private final String value;
    private final String label;

    PPictureApplyTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, PPictureApplyTypeEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PPictureApplyTypeEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    public static Optional<PPictureApplyTypeEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
