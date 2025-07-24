package com.lz.points.service;

import com.lz.points.model.dto.pay.PayRequest;
import com.lz.points.manager.model.AlipayPcPaymentVo;
import com.lz.points.model.vo.paymentOrderInfo.UserPaymentOrderInfoVo;

import java.util.HashMap;

public interface IPayService {
    /**
     * 支付宝支付web端
     *
     * @param payRequest 支付参数
     * @return AlipayPcPaymentVo
     * @author: YY
     * @method: alipayWeb
     * @date: 2025/5/15 09:47
     **/
    AlipayPcPaymentVo alipayWeb(PayRequest payRequest);

    /**
     * 阿里支付回调方法
     *
     * @param alipayCallbackRequest
     * @return void
     * @author: YY
     * @method: alipayCallback
     * @date: 2025/5/16 14:00
     **/
    String alipayCallback(HashMap<String, String> alipayCallbackRequest);

    /**
     * 获取支付信息 数据库存储
     *
     * @param outTradeNo 订单号
     * @param userId     用户编号
     * @return UserPaymentOrderInfoVo
     * @author: YY
     * @method: getOrderInfo
     * @date: 2025/5/18 19:24
     **/
    UserPaymentOrderInfoVo getAlipayWebOrder(String outTradeNo, String userId);

    UserPaymentOrderInfoVo getAlipayWeb(String outTradeNo, String userId);
}
