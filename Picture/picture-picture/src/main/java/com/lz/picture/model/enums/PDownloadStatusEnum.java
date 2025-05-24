package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 下载状态 枚举
 */
@Getter
public enum PDownloadStatusEnum {
    DOWNLOAD_STATUS_0("0", "成功"),
    DOWNLOAD_STATUS_1("1", "失败");

    private static final Map<String, PDownloadStatusEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PDownloadStatusEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PDownloadStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static Optional<PDownloadStatusEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
