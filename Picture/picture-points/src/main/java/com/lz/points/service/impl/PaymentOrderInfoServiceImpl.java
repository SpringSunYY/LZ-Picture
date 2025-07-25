package com.lz.points.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

import com.lz.common.utils.DateUtils;
import com.lz.points.model.enums.PoOrderStatusEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.points.mapper.PaymentOrderInfoMapper;
import com.lz.points.model.domain.PaymentOrderInfo;
import com.lz.points.service.IPaymentOrderInfoService;
import com.lz.points.model.dto.paymentOrderInfo.PaymentOrderInfoQuery;
import com.lz.points.model.vo.paymentOrderInfo.PaymentOrderInfoVo;

/**
 * 支付订单Service业务层处理
 *
 * @author YY
 * @date 2025-05-17
 */
@Service
public class PaymentOrderInfoServiceImpl extends ServiceImpl<PaymentOrderInfoMapper, PaymentOrderInfo> implements IPaymentOrderInfoService {
    @Resource
    private PaymentOrderInfoMapper paymentOrderInfoMapper;

    //region mybatis代码

    /**
     * 查询支付订单
     *
     * @param orderId 支付订单主键
     * @return 支付订单
     */
    @Override
    public PaymentOrderInfo selectPaymentOrderInfoByOrderId(String orderId) {
        return paymentOrderInfoMapper.selectPaymentOrderInfoByOrderId(orderId);
    }

    /**
     * 查询支付订单列表
     *
     * @param paymentOrderInfo 支付订单
     * @return 支付订单
     */
    @Override
    public List<PaymentOrderInfo> selectPaymentOrderInfoList(PaymentOrderInfo paymentOrderInfo) {
        return paymentOrderInfoMapper.selectPaymentOrderInfoList(paymentOrderInfo);
    }

    /**
     * 新增支付订单
     *
     * @param paymentOrderInfo 支付订单
     * @return 结果
     */
    @Override
    public int insertPaymentOrderInfo(PaymentOrderInfo paymentOrderInfo) {
        paymentOrderInfo.setCreateTime(DateUtils.getNowDate());
        return paymentOrderInfoMapper.insertPaymentOrderInfo(paymentOrderInfo);
    }

    /**
     * 修改支付订单
     *
     * @param paymentOrderInfo 支付订单
     * @return 结果
     */
    @Override
    public int updatePaymentOrderInfo(PaymentOrderInfo paymentOrderInfo) {
        paymentOrderInfo.setUpdateTime(DateUtils.getNowDate());
        return paymentOrderInfoMapper.updatePaymentOrderInfo(paymentOrderInfo);
    }

    /**
     * 批量删除支付订单
     *
     * @param orderIds 需要删除的支付订单主键
     * @return 结果
     */
    @Override
    public int deletePaymentOrderInfoByOrderIds(String[] orderIds) {
        return paymentOrderInfoMapper.deletePaymentOrderInfoByOrderIds(orderIds);
    }

    /**
     * 删除支付订单信息
     *
     * @param orderId 支付订单主键
     * @return 结果
     */
    @Override
    public int deletePaymentOrderInfoByOrderId(String orderId) {
        return paymentOrderInfoMapper.deletePaymentOrderInfoByOrderId(orderId);
    }

    //endregion
    @Override
    public QueryWrapper<PaymentOrderInfo> getQueryWrapper(PaymentOrderInfoQuery paymentOrderInfoQuery) {
        QueryWrapper<PaymentOrderInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = paymentOrderInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String orderId = paymentOrderInfoQuery.getOrderId();
        queryWrapper.eq(StringUtils.isNotEmpty(orderId), "order_id", orderId);

        String userId = paymentOrderInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String orderType = paymentOrderInfoQuery.getOrderType();
        queryWrapper.eq(StringUtils.isNotEmpty(orderType), "order_type", orderType);

        String orderStatus = paymentOrderInfoQuery.getOrderStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(orderStatus), "order_status", orderStatus);

        String paymentType = paymentOrderInfoQuery.getPaymentType();
        queryWrapper.eq(StringUtils.isNotEmpty(paymentType), "payment_type", paymentType);

        String thirdParty = paymentOrderInfoQuery.getThirdParty();
        queryWrapper.eq(StringUtils.isNotEmpty(thirdParty), "third_party", thirdParty);

        String thirdUserId = paymentOrderInfoQuery.getThirdUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(thirdUserId), "third_user_id", thirdUserId);

        String thirdPartyOrder = paymentOrderInfoQuery.getThirdPartyOrder();
        queryWrapper.eq(StringUtils.isNotEmpty(thirdPartyOrder), "third_party_order", thirdPartyOrder);

        Date paymentTime = paymentOrderInfoQuery.getPaymentTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginPaymentTime")) && StringUtils.isNotNull(params.get("endPaymentTime")), "payment_time", params.get("beginPaymentTime"), params.get("endPaymentTime"));

        String paymentStatus = paymentOrderInfoQuery.getPaymentStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(paymentStatus), "payment_status", paymentStatus);

        String paymentCode = paymentOrderInfoQuery.getPaymentCode();
        queryWrapper.eq(StringUtils.isNotEmpty(paymentCode), "payment_code", paymentCode);

        String paymentMsg = paymentOrderInfoQuery.getPaymentMsg();
        queryWrapper.eq(StringUtils.isNotEmpty(paymentMsg), "payment_msg", paymentMsg);


        Date createTime = paymentOrderInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date updateTime = paymentOrderInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        String deviceId = paymentOrderInfoQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId), "device_id", deviceId);

        String browser = paymentOrderInfoQuery.getBrowser();
        queryWrapper.eq(StringUtils.isNotEmpty(browser), "browser", browser);

        String os = paymentOrderInfoQuery.getOs();
        queryWrapper.like(StringUtils.isNotEmpty(os), "os", os);

        String platform = paymentOrderInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform), "platform", platform);

        String ipAddr = paymentOrderInfoQuery.getIpAddr();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddr), "ip_addr", ipAddr);

        String ipAddress = paymentOrderInfoQuery.getIpAddress();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddress), "ip_address", ipAddress);

        String isDelete = paymentOrderInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete), "is_delete", isDelete);

        return queryWrapper;
    }


    @Override
    public List<PaymentOrderInfoVo> convertVoList(List<PaymentOrderInfo> paymentOrderInfoList) {
        if (StringUtils.isEmpty(paymentOrderInfoList)) {
            return Collections.emptyList();
        }
        return paymentOrderInfoList.stream().map(PaymentOrderInfoVo::objToVo).collect(Collectors.toList());
    }

}
