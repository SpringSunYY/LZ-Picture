package com.lz.common.constant.redis;

/**
 * ai模块redis缓存键常量
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-08  23:55
 * @Version: 1.0
 */
public class AiRedisConstants {
    /**
     * 图片详细信息
     */
    public static final String AI_MODEL_DETAIL = "ai:model:detail";


    /**
     * 图片详细信息缓存时间
     */
    public static final int AI_MODEL_DETAIL_EXPIRE_TIME = 5 * 60;
    public static final String AI_MODEL_LIST = "ai:model:list";
    public static final int AI_MODEL_LIST_EXPIRE_TIME = 5 * 60;

    /**
     * 生成记录缓存
     */
    public static final String AI_GENERATE_LIST = "ai:generate:list";
    public static final int AI_GENERATE_LIST_EXPIRE_TIME = 10 * 60;
}
