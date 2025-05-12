package com.lz.config.model.enmus;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 模板类型 枚举
 */
@Getter
public enum CTemplateTypeEnum {
    TEMPLATE_TYPE_0("0", "其他"),
    TEMPLATE_TYPE_1("1", "短信"),
    TEMPLATE_TYPE_2("2", "邮件"),
    TEMPLATE_TYPE_3("3", "站内通知"),
    TEMPLATE_TYPE_5("5", "APP推送"),
    TEMPLATE_TYPE_6("6", "微信");

    private static final Map<String, CTemplateTypeEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (CTemplateTypeEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    CTemplateTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举值
     * @return 对应的枚举对象，若无对应项则返回 Optional.empty()
     */
    public static Optional<CTemplateTypeEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
