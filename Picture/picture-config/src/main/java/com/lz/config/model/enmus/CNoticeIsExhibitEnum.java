package com.lz.config.model.enmus;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 公告是否展示 枚举
 */
@Getter
public enum CNoticeIsExhibitEnum {

    /** 否 */
    NOTICE_IS_EXHIBIT_0("0", "否"),

    /** 是 */
    NOTICE_IS_EXHIBIT_1("1", "是");

    private final String value;
    private final String label;

    CNoticeIsExhibitEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, CNoticeIsExhibitEnum> VALUE_MAP = new HashMap<>();

    static {
        for (CNoticeIsExhibitEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    public static Optional<CNoticeIsExhibitEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
