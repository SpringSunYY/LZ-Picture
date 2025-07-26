package com.lz.config.model.enmus;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 公告状态 枚举
 */
@Getter
public enum CNoticeStatusEnum {

    /** 隐藏 */
    NOTICE_STATUS_0("0", "隐藏"),

    /** 展示 */
    NOTICE_STATUS_1("1", "展示");

    private final String value;
    private final String label;

    CNoticeStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, CNoticeStatusEnum> VALUE_MAP = new HashMap<>();

    static {
        for (CNoticeStatusEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    public static Optional<CNoticeStatusEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
