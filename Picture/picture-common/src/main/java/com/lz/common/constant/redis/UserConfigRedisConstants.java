package com.lz.common.constant.redis;

/**
 * Project: Picture
 * Package: com.lz.common.constant.redis
 * Author: YY
 * CreateTime: 2025-04-18  22:09
 * Description: UserConfigRedisConstants
 * Version: 1.0
 */
public class UserConfigRedisConstants {
    /**
     * 配置信息缓存key
     */
    public static final String CONFIG_CONFIG_INFO_KEY = "config:config:info:";

    /**
     * Localization 国际化
     */
    public static final String CONFIG_LOCALIZATION = "config:localization:";

    /**
     * 菜单权限
     */
    public static final String CONFIG_MENU_PERMISSION = "config:menu:permission:";

    /**
     * 菜单列表
     */
    public static final String CONFIG_MENU_LIST = "config:menu:list";

    /**
     * 模版缓存
     */
    public static final String CONFIG_TEMPLATE_INFO = "config:template:info:";

    /**
     * 公告缓存
     */
    public static final String CONFIG_NOTICE_INFO = "config:notice:info:";
    /**
     * 缓存超时时间
     */
    public static final int CONFIG_NOTICE_INFO_EXPIRE_TIME = 60 * 60 * 24;
}
