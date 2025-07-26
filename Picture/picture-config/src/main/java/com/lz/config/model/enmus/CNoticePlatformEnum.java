package com.lz.config.model.enmus;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 公告推送平台 枚举
 */
@Getter
public enum CNoticePlatformEnum {

    /** 全部 */
    NOTICE_PLATFORM_0("0", "全部"),

    /** Web */
    NOTICE_PLATFORM_1("1", "Web"),

    /** 小程序 */
    NOTICE_PLATFORM_2("2", "小程序");

    private final String value;
    private final String label;

    CNoticePlatformEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, CNoticePlatformEnum> VALUE_MAP = new HashMap<>();

    static {
        for (CNoticePlatformEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    public static Optional<CNoticePlatformEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
