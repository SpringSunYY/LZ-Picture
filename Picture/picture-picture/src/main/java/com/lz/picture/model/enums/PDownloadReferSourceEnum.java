package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 下载来源类型 枚举
 */
@Getter
public enum PDownloadReferSourceEnum {
    /**
     * 其他
     */
    DOWNLOAD_REFER_SOURCE_0("0", "其他"),
    /**
     * 详情
     */
    DOWNLOAD_REFER_SOURCE_1("1", "详情"),
    /**
     * 分享
     */
    DOWNLOAD_REFER_SOURCE_2("2", "分享");

    private static final Map<String, PDownloadReferSourceEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PDownloadReferSourceEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    PDownloadReferSourceEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static Optional<PDownloadReferSourceEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) return Optional.empty();
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
