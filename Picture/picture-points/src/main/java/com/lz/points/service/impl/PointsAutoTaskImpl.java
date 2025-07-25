package com.lz.points.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.utils.DateUtils;
import com.lz.points.model.domain.PaymentOrderInfo;
import com.lz.points.model.domain.PointsRechargeInfo;
import com.lz.points.model.domain.PointsRechargePackageInfo;
import com.lz.points.model.enums.PoOrderStatusEnum;
import com.lz.points.model.enums.PoPackageIsLongTermEnum;
import com.lz.points.model.enums.PoPackageStatusEnum;
import com.lz.points.model.enums.PoRechargeStatusEnum;
import com.lz.points.service.IPaymentOrderInfoService;
import com.lz.points.service.IPointsAutoTask;
import com.lz.points.service.IPointsRechargeInfoService;
import com.lz.points.service.IPointsRechargePackageInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 自动更新积分任务
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-07-25  21:44
 * @Version: 1.0
 */
@Service
public class PointsAutoTaskImpl implements IPointsAutoTask {
    @Resource
    private IPointsRechargePackageInfoService pointsRechargePackageInfoService;
    @Resource
    private IPaymentOrderInfoService paymentOrderInfoService;
    @Resource
    private IPointsRechargeInfoService pointsRechargeInfoService;

    @Override
    public int autoUpdateRechargeExpiredOrder(Date expiredTime) {
        //查询到未支付且超过过期时间的订单
        List<PointsRechargeInfo> list = pointsRechargeInfoService.list(new LambdaQueryWrapper<PointsRechargeInfo>()
                .lt(PointsRechargeInfo::getCreateTime, expiredTime)
                .eq(PointsRechargeInfo::getRechargeStatus, PoRechargeStatusEnum.RECHARGE_STATUS_0.getValue()));
        //更新为过期
        list.forEach(item -> {
            item.setRechargeStatus(PoRechargeStatusEnum.RECHARGE_STATUS_3.getValue());
        });
        return pointsRechargeInfoService.updateBatchById(list) ? 1 : 0;
    }

    @Override
    public int autoUpdateExpiredOrder(Date expiredTime) {
        //查询到过期的订单 未支付的订单
        List<PaymentOrderInfo> paymentOrderInfos = paymentOrderInfoService.list(new LambdaQueryWrapper<PaymentOrderInfo>()
                .eq(PaymentOrderInfo::getOrderStatus, PoOrderStatusEnum.ORDER_STATUS_0.getValue())
                .lt(PaymentOrderInfo::getCreateTime, expiredTime));
        //更新订单为已过期
        paymentOrderInfos.forEach(paymentOrderInfo ->
                paymentOrderInfo.setOrderStatus(PoOrderStatusEnum.ORDER_STATUS_3.getValue())
        );
        return paymentOrderInfoService.updateBatchById(paymentOrderInfos) ? 1 : 0;
    }

    @Override
    public void autoUpdatePointsRechargePackageInfo() {
        Date nowDate = DateUtils.getNowDate();
        //查询到未开始订单
        List<PointsRechargePackageInfo> packageInfos = pointsRechargePackageInfoService.list(new LambdaQueryWrapper<PointsRechargePackageInfo>()
                .eq(PointsRechargePackageInfo::getIsLongTerm, PoPackageIsLongTermEnum.PACKAGE_IS_LONG_TERM_1.getValue())
                .eq(PointsRechargePackageInfo::getPackageStatus, PoPackageStatusEnum.PACKAGE_STATUS_0.getValue())
                .le(PointsRechargePackageInfo::getStartTime, nowDate));
        for (PointsRechargePackageInfo packageInfo : packageInfos) {
            packageInfo.setPackageStatus(PoPackageStatusEnum.PACKAGE_STATUS_1.getValue());
        }
        pointsRechargePackageInfoService.updateBatchById(packageInfos);

        //查询到进行中的订单
        List<PointsRechargePackageInfo> list = pointsRechargePackageInfoService.list(new LambdaQueryWrapper<PointsRechargePackageInfo>()
                .eq(PointsRechargePackageInfo::getIsLongTerm, PoPackageIsLongTermEnum.PACKAGE_IS_LONG_TERM_1.getValue())
                .eq(PointsRechargePackageInfo::getPackageStatus, PoPackageStatusEnum.PACKAGE_STATUS_1.getValue())
                .le(PointsRechargePackageInfo::getEndTime, nowDate));
        for (PointsRechargePackageInfo packageInfo : list) {
            packageInfo.setPackageStatus(PoPackageStatusEnum.PACKAGE_STATUS_2.getValue());
        }
        pointsRechargePackageInfoService.updateBatchById(list);
    }
}
