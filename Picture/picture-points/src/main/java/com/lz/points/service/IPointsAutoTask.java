package com.lz.points.service;

import org.springframework.stereotype.Service;

import java.util.Date;


public interface IPointsAutoTask {

    /**
     * 自动更新过期订单
     *
     * @param expiredTime 超时时间
     */
    int autoUpdateExpiredOrder(Date expiredTime);

    /**
     * 自动更新过期订单
     *
     * @param expiredTime 过期时间
     */
    int autoUpdateRechargeExpiredOrder(Date expiredTime);

    /**
     * 自动更新充值套餐信息
     */
    void autoUpdatePointsRechargePackageInfo();
}
