package com.lz.points.service.impl;

import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.points.config.AlipayPaymentConfig;
import com.lz.points.manager.AlipayManager;
import com.lz.points.manager.model.AlipayPcPaymentRequest;
import com.lz.points.manager.model.AlipayPcPaymentResponse;
import com.lz.points.model.domain.PointsRechargePackageInfo;
import com.lz.points.model.dto.pay.PayRequest;
import com.lz.points.model.enums.PoPackageIsLongTermEnum;
import com.lz.points.model.enums.PoPackageStatusEnum;
import com.lz.points.model.vo.pay.AlipayPcPaymentVo;
import com.lz.points.service.IPayService;
import com.lz.points.service.IPointsRechargePackageInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-15  09:39
 * @Version: 1.0
 */
@Service
public class PayServiceImpl implements IPayService {
    @Resource
    private IPointsRechargePackageInfoService pointsRechargePackageInfoService;
    @Resource
    private AlipayManager alipayManager;
    @Resource
    private AlipayPaymentConfig alipayPaymentConfig;

    @Override
    public AlipayPcPaymentVo alipayWeb(PayRequest payRequest) {
        //首先查询套餐详情
        PointsRechargePackageInfo pointsRechargePackageInfo = pointsRechargePackageInfoService.selectPointsRechargePackageInfoByPackageId(payRequest.getPackageId());
        ThrowUtils.throwIf(StringUtils.isNull(pointsRechargePackageInfo), "套餐不存在!!!");
        //状态是否正常
        ThrowUtils.throwIf(!pointsRechargePackageInfo.getPackageStatus().equals(PoPackageStatusEnum.PACKAGE_STATUS_1.getValue()), "套餐未开始!!!");
        //如果套餐不是长期套餐，判断是否在时间范围内
        if (!pointsRechargePackageInfo.getIsLongTerm().equals(PoPackageIsLongTermEnum.PACKAGE_IS_LONG_TERM_0.getValue())) {
            ThrowUtils.throwIf(pointsRechargePackageInfo.getStartTime().getTime() > System.currentTimeMillis(), "套餐未开始!!!");
            ThrowUtils.throwIf(pointsRechargePackageInfo.getEndTime().getTime() < System.currentTimeMillis(), "套餐已结束!!!");
        }
        AlipayPcPaymentRequest alipayPcPaymentRequest = new AlipayPcPaymentRequest();
        alipayPcPaymentRequest.setTotalAmount(pointsRechargePackageInfo.getPrice());
        alipayPcPaymentRequest.setOutTradeNo(IdUtils.snowflakeId().toString());
        alipayPcPaymentRequest.setSubject(pointsRechargePackageInfo.getPackageName());
        alipayPcPaymentRequest.setBuyerId(payRequest.getUserId());
        alipayPcPaymentRequest.setTimeoutExpress(alipayPaymentConfig.getTimeoutExpress());
        alipayPcPaymentRequest.setProductCode(alipayPaymentConfig.getProductCode());

        AlipayPcPaymentResponse alipayPcPaymentResponse = alipayManager.pcPay(alipayPcPaymentRequest);
        AlipayPcPaymentVo paymentVo = new AlipayPcPaymentVo();
        BeanUtils.copyProperties(alipayPcPaymentResponse, paymentVo);
        return paymentVo;
    }
}
