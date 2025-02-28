// src/constants/PermissionStatusConstants.js

/**
 * 权限状态常量
 */
export const PERMISSION_STATUS = {
    // 正常
    NORMAL: '0',
    // 隐藏
    HIDDEN: '1'
};

/**
 * 权限状态选项列表（用于下拉框）
 */
export const PERMISSION_STATUS_OPTIONS = [
    { label: '正常', value: PERMISSION_STATUS.NORMAL },
    { label: '隐藏', value: PERMISSION_STATUS.HIDDEN }
];

/**
 * 权限状态值 -> 标签映射
 */
export const PERMISSION_STATUS_MAP = {
    [PERMISSION_STATUS.NORMAL]: '正常',
    [PERMISSION_STATUS.HIDDEN]: '隐藏'
};

/**
 * 区域状态常量
 */
export const LOCALE_STATUS = {
    // 正常
    NORMAL: '0',
    // 隐藏
    HIDDEN: '1'
};

/**
 * 区域状态选项列表
 */
export const LOCALE_STATUS_OPTIONS = [
    { label: '正常', value: LOCALE_STATUS.NORMAL },
    { label: '隐藏', value: LOCALE_STATUS.HIDDEN }
];

/**
 * 区域状态值 -> 标签映射
 */
export const LOCALE_STATUS_MAP = {
    [LOCALE_STATUS.NORMAL]: '正常',
    [LOCALE_STATUS.HIDDEN]: '隐藏'
};

/**
 * 配置类型常量
 */
export const CONFIG_TYPE = {
    // 值
    VALUE: '1',
    // 文件
    FILE: '2'
};

/**
 * 配置类型选项列表
 */
export const CONFIG_TYPE_OPTIONS = [
    { label: '值', value: CONFIG_TYPE.VALUE },
    { label: '文件', value: CONFIG_TYPE.FILE }
];

/**
 * 配置类型值 -> 标签映射
 */
export const CONFIG_TYPE_MAP = {
    [CONFIG_TYPE.VALUE]: '值',
    [CONFIG_TYPE.FILE]: '文件'
};
