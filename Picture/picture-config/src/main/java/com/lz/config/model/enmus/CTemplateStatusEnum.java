package com.lz.config.model.enmus;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 模板状态 枚举
 */
@Getter
public enum CTemplateStatusEnum {
    TEMPLATE_STATUS_0("0", "已启用"),
    TEMPLATE_STATUS_1("1", "已禁用");

    private static final Map<String, CTemplateStatusEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (CTemplateStatusEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    CTemplateStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举值
     * @return 对应的枚举对象，若无对应项则返回 Optional.empty()
     */
    public static Optional<CTemplateStatusEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
