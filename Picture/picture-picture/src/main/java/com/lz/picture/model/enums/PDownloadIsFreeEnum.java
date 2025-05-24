package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 是否免费下载 枚举
 */
@Getter
public enum PDownloadIsFreeEnum {
    /**
     * 是
     */
    DOWNLOAD_IS_FREE_0("0", "是"),
    /**
     * 否
     */
    DOWNLOAD_IS_FREE_1("1", "否");

    private static final Map<String, PDownloadIsFreeEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PDownloadIsFreeEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PDownloadIsFreeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static Optional<PDownloadIsFreeEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) return Optional.empty();
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
