package com.lz.picture.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 空间邀请状态 枚举
 */
@Getter
public enum PSpaceInvitationStatusEnum {

    /**
     * 待同意
     */
    SPACE_INVITATION_STATUS_0("0", "待同意"),

    /**
     * 同意
     */
    SPACE_INVITATION_STATUS_1("1", "同意"),

    /**
     * 拒绝
     */
    SPACE_INVITATION_STATUS_2("2", "拒绝"),

    /**
     * 过期
     */
    SPACE_INVITATION_STATUS_3("3", "过期"),

    /**
     * 已取消
     */
    SPACE_INVITATION_STATUS_4("4", "已取消");

    private final String value;
    private final String label;

    PSpaceInvitationStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private static final Map<String, PSpaceInvitationStatusEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PSpaceInvitationStatusEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    /**
     * 根据值获取枚举对象
     *
     * @param value 字符串值
     * @return 可选枚举对象
     */
    public static Optional<PSpaceInvitationStatusEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }
}
