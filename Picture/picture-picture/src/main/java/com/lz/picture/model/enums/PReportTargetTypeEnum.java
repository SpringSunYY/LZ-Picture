package com.lz.picture.model.enums;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 举报目标类型 枚举
 */
@Getter
public enum PReportTargetTypeEnum {

    /** 图片 */
    P_REPORT_TARGET_TYPE_0("0", "图片"),

    /** 空间 */
    P_REPORT_TARGET_TYPE_1("1", "空间"),

    /** 用户 */
    P_REPORT_TARGET_TYPE_2("2", "用户");

    private final String value;
    private final String label;

    PReportTargetTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, PReportTargetTypeEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PReportTargetTypeEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    public static Optional<PReportTargetTypeEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
