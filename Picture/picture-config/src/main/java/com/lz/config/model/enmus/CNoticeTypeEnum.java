package com.lz.config.model.enmus;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 公告类型 枚举
 */
@Getter
public enum CNoticeTypeEnum {

    /** 其他 */
    NOTICE_TYPE_0("0", "其他"),

    /** 平台推送 */
    NOTICE_TYPE_1("1", "平台推送"),

    /** 用户须知 */
    NOTICE_TYPE_2("2", "用户须知");

    private final String value;
    private final String label;

    CNoticeTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, CNoticeTypeEnum> VALUE_MAP = new HashMap<>();

    static {
        for (CNoticeTypeEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    public static Optional<CNoticeTypeEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
