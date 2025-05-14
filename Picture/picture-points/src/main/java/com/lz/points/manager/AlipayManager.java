package com.lz.points.manager;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.lz.points.config.AlipayConfig;
import com.lz.points.manager.model.AlipayPcPaymentRequest;
import com.lz.points.manager.model.AlipayPcPaymentResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

import static com.lz.points.config.AlipayConfig.getAlipayConfig;

/**
 * TODO
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-14  21:22
 * @Version: 1.0
 */
@Slf4j
public class AlipayManager {
    public AlipayPcPaymentResponse pcPay(AlipayPcPaymentRequest alipayPcPaymentRequest) {

        try {
            // 初始化SDK
            AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig());
            // 构造请求参数以调用接口
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            request.setReturnUrl(AlipayConfig.notifyUrl);
            AlipayTradePagePayModel model = new AlipayTradePagePayModel();
            request.setNotifyUrl("");
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", alipayPcPaymentRequest.getOutTradeNo());
            bizContent.put("total_amount", alipayPcPaymentRequest.getTotalAmount());
            bizContent.put("subject", alipayPcPaymentRequest.getSubject());
            bizContent.put("buyer_id", alipayPcPaymentRequest.getBuyerId());
            bizContent.put("timeout_express", alipayPcPaymentRequest.getTimeoutExpress());
            bizContent.put("product_code", alipayPcPaymentRequest.getProductCode());
            request.setBizContent(bizContent.toString());

            // 第三方代调用模式下请设置app_auth_token
            // request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");

            AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "POST");
            System.out.println("response = " + response);
            // 如果需要返回GET请求，请使用
            // AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "GET");
            String pageRedirectionData = response.getBody();
            System.out.println(pageRedirectionData);

            AlipayPcPaymentResponse result = new AlipayPcPaymentResponse();
            if (response.isSuccess()) {
                System.out.println("调用成功");
                StringBuffer sb = new StringBuffer();
                sb.append("<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>Title</title>\n" +
                        "</head>\n" +
                        "<body>");
                sb.append(pageRedirectionData);
                sb.append("</body>\n" +
                        "</html>");
                result.setBody(sb.toString());
            } else {
                System.out.println("调用失败");
                // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
                // String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
                // System.out.println(diagnosisUrl);
            }
            result.setMsg(response.getMsg());
            result.setPageRedirectionData(pageRedirectionData);
            return result;
        } catch (AlipayApiException e) {
            log.error("支付请求失败！！！", e);
            throw new RuntimeException("支付请求失败！！！");
        }
    }
}
