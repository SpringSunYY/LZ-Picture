package com.lz.ai.model.enums;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * AI日志状态
 */
@Getter
public enum AiLogStatusEnum {

    /** 请求中 */
    LOG_STATUS_0("0", "请求中"),

    /** 成功 */
    LOG_STATUS_1("1", "成功"),

    /** 失败 */
    LOG_STATUS_2("2", "失败"),

    /** 超时 */
    LOG_STATUS_3("3", "超时");

    private final String value;
    private final String label;

    AiLogStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, AiLogStatusEnum> VALUE_MAP = new HashMap<>();

    static {
        for (AiLogStatusEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    public static Optional<AiLogStatusEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
