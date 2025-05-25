package com.lz.points;

import com.lz.points.manager.AlipayManager;
import com.lz.points.manager.model.AlipayPcPaymentRequest;
import com.lz.points.manager.model.AlipayPcPaymentResponse;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

/**
 * 支付测试
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-14  23:22
 * @Version: 1.0
 */
@SpringBootTest
public class AlipayTest {
    @Resource
    private AlipayManager alipayManager;

    @Test
    public void testPcPayment(){
        AlipayPcPaymentRequest alipayPcPaymentRequest = new AlipayPcPaymentRequest();
        alipayPcPaymentRequest.setTotalAmount(BigDecimal.valueOf(100));
        alipayPcPaymentRequest.setOutTradeNo(String.valueOf(System.currentTimeMillis()));
        alipayPcPaymentRequest.setSubject("一元基础套餐");
        alipayPcPaymentRequest.setBuyerId("YYY0908");
        alipayPcPaymentRequest.setTimeoutExpress("10m");
        alipayPcPaymentRequest.setProductCode("FAST_INSTANT_TRADE_PAY");

        AlipayPcPaymentResponse alipayPcPaymentResponse = alipayManager.pcPay(alipayPcPaymentRequest);
        System.err.println("alipayPcPaymentResponse = " + alipayPcPaymentResponse);
    }
}
