package com.lz.picture.model.enums;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 举报审核状态 枚举
 */
@Getter
public enum PReportReviewStatusEnum {

    /** 待审核 */
    P_REPORT_REVIEW_STATUS_0("0", "待审核"),

    /** 同意 */
    P_REPORT_REVIEW_STATUS_1("1", "同意"),

    /** 拒绝 */
    P_REPORT_REVIEW_STATUS_2("2", "拒绝");

    private final String value;
    private final String label;

    PReportReviewStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, PReportReviewStatusEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PReportReviewStatusEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    public static Optional<PReportReviewStatusEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
