package com.lz.points.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.points.config.AlipayPaymentConfig;
import com.lz.points.manager.AlipayManager;
import com.lz.points.manager.model.AlipayCallbackRequest;
import com.lz.points.manager.model.AlipayPcPaymentRequest;
import com.lz.points.manager.model.AlipayPcPaymentResponse;
import com.lz.points.model.domain.PaymentOrderInfo;
import com.lz.points.model.domain.PointsRechargeInfo;
import com.lz.points.model.domain.PointsRechargePackageInfo;
import com.lz.points.model.dto.pay.PayRequest;
import com.lz.points.model.enums.PoPackageIsLongTermEnum;
import com.lz.points.model.enums.PoPackageStatusEnum;
import com.lz.points.model.enums.PoPaymentTypeEnum;
import com.lz.points.model.enums.PoRechargeStatusEnum;
import com.lz.points.model.vo.pay.AlipayPcPaymentVo;
import com.lz.points.service.IPayService;
import com.lz.points.service.IPaymentOrderInfoService;
import com.lz.points.service.IPointsRechargeInfoService;
import com.lz.points.service.IPointsRechargePackageInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import static com.lz.points.constant.PayConstants.ALIPAY_WEB;
import static com.lz.points.constant.PayConstants.WAIT_BUYER_PAY;

/**
 * TODO
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-15  09:39
 * @Version: 1.0
 */
@Slf4j
@Service
public class PayServiceImpl implements IPayService {
    @Resource
    private IPointsRechargePackageInfoService pointsRechargePackageInfoService;

    @Resource
    private AlipayManager alipayManager;

    @Resource
    private AlipayPaymentConfig alipayPaymentConfig;

    @Resource
    private IPaymentOrderInfoService paymentOrderInfoService;

    @Resource
    private IPointsRechargeInfoService pointsRechargeInfoService;

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
        BigDecimal price = pointsRechargePackageInfo.getPrice();
        alipayPcPaymentRequest.setTotalAmount(price);
        payRequest.setTotalAmount(price);
        //自定义订单号
        String outTradeNo = IdUtils.snowflakeId().toString();
        payRequest.setOutTradeNo(outTradeNo);
        alipayPcPaymentRequest.setOutTradeNo(outTradeNo);
        alipayPcPaymentRequest.setSubject(pointsRechargePackageInfo.getPackageName());
        alipayPcPaymentRequest.setBuyerId(payRequest.getUserId());
        alipayPcPaymentRequest.setTimeoutExpress(alipayPaymentConfig.getTimeoutExpress());
        alipayPcPaymentRequest.setProductCode(alipayPaymentConfig.getProductCode());

        AlipayPcPaymentResponse alipayPcPaymentResponse = alipayManager.pcPay(alipayPcPaymentRequest);
        AlipayPcPaymentVo paymentVo = new AlipayPcPaymentVo();
        BeanUtils.copyProperties(alipayPcPaymentResponse, paymentVo);
        //创建订单
        createOrder(payRequest, pointsRechargePackageInfo);
        return paymentVo;
    }

    private void createOrder(PayRequest request, PointsRechargePackageInfo packageInfo) {
        //支付信息
        PaymentOrderInfo orderInfo = new PaymentOrderInfo();
        orderInfo.setOrderId(request.getOutTradeNo());
        orderInfo.setUserId(request.getUserId());
        orderInfo.setOrderStatus(PoPackageStatusEnum.PACKAGE_STATUS_0.getValue());
        orderInfo.setPaymentType(PoPaymentTypeEnum.PAYMENT_TYPE_0.getValue());
        orderInfo.setTotalAmount(request.getTotalAmount());
        orderInfo.setThirdParty(ALIPAY_WEB);
        orderInfo.setPaymentStatus(WAIT_BUYER_PAY);
        orderInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
        //获取设备信息
        DeviceInfo deviceInfo = IpUtils.getDeviceInfo();
        BeanUtils.copyProperties(deviceInfo, orderInfo);
        orderInfo.setIpAddr(deviceInfo.getIpaddr());
        paymentOrderInfoService.insertPaymentOrderInfo(orderInfo);
        //充值记录信息
        PointsRechargeInfo rechargeInfo = new PointsRechargeInfo();
        rechargeInfo.setRechargeId(IdUtils.snowflakeId().toString());
        rechargeInfo.setPackageId(request.getPackageId());
        rechargeInfo.setUserId(request.getUserId());
        rechargeInfo.setOrderId(request.getOutTradeNo());
        //先写死
        rechargeInfo.setTotalCount(1L);
        Long points = StringUtils.isNotNull(packageInfo.getPoints()) ? packageInfo.getPoints() : 0L;
        Long bonus = StringUtils.isNotNull(packageInfo.getPointsBonus()) ? packageInfo.getPointsBonus() : 0L;
        rechargeInfo.setTotalCount(points + bonus);
        rechargeInfo.setPointsCount(points);
        rechargeInfo.setBonusCount(bonus);
        rechargeInfo.setPriceCount(request.getTotalAmount());
        rechargeInfo.setBuyerPayAmount(new BigDecimal(BigInteger.ZERO));
        rechargeInfo.setThirdParty(ALIPAY_WEB);
        rechargeInfo.setRechargeStatus(PoRechargeStatusEnum.RECHARGE_STATUS_0.getValue());
        BeanUtils.copyProperties(deviceInfo, rechargeInfo);
        rechargeInfo.setIpAddr(deviceInfo.getIpaddr());
        pointsRechargeInfoService.insertPointsRechargeInfo(rechargeInfo);
    }

    @Override
    public void alipayCallback(HashMap<String, String> map) {
        boolean sign = false;
        try {
            sign = AlipaySignature.rsaCheckV1(map,
                    alipayPaymentConfig.getPublicKey(),
                    alipayPaymentConfig.getCharset(),
                    alipayPaymentConfig.getSignType());
        } catch (AlipayApiException e) {
            log.error("获取支付宝签名失败！！！", e);
            throw new RuntimeException("获取支付宝签名失败！！！");
        }

        System.out.println("sign = " + sign);
        if (!sign) {
            throw new RuntimeException("签名验证失败！！！");
        } else {
            //转换map为json
            String json = JSON.toJSONString(map);
            //转换JSON为阿里支付回调请求参数实体
            AlipayCallbackRequest alipayCallbackRequest = JSON.parseObject(json, AlipayCallbackRequest.class); //转换map为json
            alipayManager.query(alipayCallbackRequest.getOutTradeNo(), alipayCallbackRequest.getTradeNo());
        }
    }
}
