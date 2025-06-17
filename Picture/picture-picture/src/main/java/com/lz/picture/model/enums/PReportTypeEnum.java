package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 举报类型 枚举
 */
@Getter
public enum PReportTypeEnum {

    /** 侵权 */
    P_REPORT_TYPE_0("0", "侵权"),

    /** 血腥暴力 */
    P_REPORT_TYPE_1("1", "血腥暴力"),

    /** 涉黄 */
    P_REPORT_TYPE_2("2", "涉黄");

    private final String value;
    private final String label;

    PReportTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, PReportTypeEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PReportTypeEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<PReportTypeEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
