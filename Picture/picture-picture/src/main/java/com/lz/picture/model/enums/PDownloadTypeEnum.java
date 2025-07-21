package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 下载方式类型 枚举
 */
@Getter
public enum PDownloadTypeEnum {
    DOWNLOAD_TYPE_0("0", "查看"),
    DOWNLOAD_TYPE_1("1", "下载"),
    DOWNLOAD_TYPE_2("2", "批量");

    private static final Map<String, PDownloadTypeEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PDownloadTypeEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PDownloadTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static Optional<PDownloadTypeEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
