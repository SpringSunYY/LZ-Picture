package com.lz.common.constant.redis;

/**
 * 积分模块redis缓存
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-18  19:28
 * @Version: 1.0
 */
public class PointsRedisConstants {
    /**
     * 订单缓存
     */
    public static final String POINTS_ORDER_DETAIL = "points:pay:order:detail:";

    /**
     * 超时时间
     */
    public static final Integer POINTS_ORDER_DETAIL_EXPIRE_TIME = 5 * 60;

    /**
     * 订单锁
     */
    public static final String POINTS_ORDER_LOCK = "points:pay:order:lock:";
}
