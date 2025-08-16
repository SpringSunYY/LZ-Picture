package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 图片上传类型
 */
@Getter
public enum PPictureUploadTypeEnum {

    /**
     * 手动上传
     */
    PICTURE_UPLOAD_TYPE_1("1", "手动上传"),

    /**
     * AI生成
     */
    PICTURE_UPLOAD_TYPE_2("2", "AI生成"),

    /**
     * API导入
     */
    PICTURE_UPLOAD_TYPE_3("3", "API导入");

    private final String value;
    private final String label;

    PPictureUploadTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, PPictureUploadTypeEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PPictureUploadTypeEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    /**
     * 根据值获取对应的枚举
     */
    public static Optional<PPictureUploadTypeEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
