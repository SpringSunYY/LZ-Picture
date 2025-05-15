package com.lz.points.service;

import com.lz.points.manager.model.AlipayPcPaymentRequest;
import com.lz.points.manager.model.AlipayPcPaymentResponse;
import com.lz.points.model.dto.pay.PayRequest;
import com.lz.points.model.vo.pay.AlipayPcPaymentVo;

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
}
