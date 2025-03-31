package com.lz.common.constant.config;

/**
 * Project: Picture
 * Package: com.lz.common.constant.redis
 * Author: YY
 * CreateTime: 2025-02-28  21:34
 * Description: ConfigKeyConstants
 * Version: 1.0
 */
public class ConfigKeyConstants {
    /**
     * 配置信息缓存key
     */
    public static final String CONFIG_CONFIG_INFO_KEY = "config:config:info:";

    /**
     * Localization 国际化
     */
    public static final String CONFIG_LOCALIZATION = "config:localization:";

    /**
     * 是否需要验证码
     */
    public static final String USER_LOGIN_CAPTCHA_ENABLED = "user:login:captchaEnabled";

    /**
     * 菜单权限
     */
    public static final String CONFIG_MENU_PERMISSION = "config:menu:permission:";

    /**
     * 空间最大文件数
     */
    public static final String PICTURE_SPACE_MAX_COUNT = "picture:space:maxCount";

    /**
     * 空间最大容量
     */
    public static final String PICTURE_SPACE_MAX_SIZE = "picture:space:maxSize";
}
