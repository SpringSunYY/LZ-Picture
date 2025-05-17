package com.lz.points.manager;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.lz.points.config.AlipayPaymentConfig;
import com.lz.points.manager.model.AlipayPcPaymentRequest;
import com.lz.points.manager.model.AlipayPcPaymentResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * 支付宝支付
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-14  21:22
 * @Version: 1.0
 */
@Slf4j
@Component
public class AlipayManager {
    @Resource(name = "alipayPaymentConfig")
    private AlipayPaymentConfig config;

    public AlipayConfig getAlipayConfig() {
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl(config.getServerUrl());
        alipayConfig.setAppId(config.getAppId());
        alipayConfig.setPrivateKey(config.getPrivateKey());
        alipayConfig.setFormat(config.getFormat());
        alipayConfig.setAlipayPublicKey(config.getPublicKey());
        alipayConfig.setCharset(config.getCharset());
        alipayConfig.setSignType(config.getSignType());
        return alipayConfig;
    }

    public AlipayPcPaymentResponse pcPay(AlipayPcPaymentRequest alipayPcPaymentRequest) {

        try {
            // 初始化SDK
            AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig());
            // 构造请求参数以调用接口
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            request.setReturnUrl(config.getNotifyUrl());
            request.setNotifyUrl("");
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", alipayPcPaymentRequest.getOutTradeNo());
            bizContent.put("total_amount", alipayPcPaymentRequest.getTotalAmount());
            bizContent.put("subject", alipayPcPaymentRequest.getSubject());
            bizContent.put("buyer_id", alipayPcPaymentRequest.getBuyerId());
            bizContent.put("timeout_express", alipayPcPaymentRequest.getTimeoutExpress());
            bizContent.put("product_code", alipayPcPaymentRequest.getProductCode());
            request.setBizContent(bizContent.toString());

            AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "POST");
            // 如果需要返回GET请求，请使用
            // AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "GET");
            String pageRedirectionData = response.getBody();

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
                result.setHtml(sb.toString());
                result.setPageRedirectionData(pageRedirectionData);
            } else {
                log.error("支付请求失败:{}", JSONObject.toJSONString(response));
                // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
                // String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
                // System.out.println(diagnosisUrl);
            }
            result.setSuccess(response.isSuccess());
            result.setOutTradeNo(alipayPcPaymentRequest.getOutTradeNo());
            return result;
        } catch (AlipayApiException e) {
            log.error("支付请求失败！！！", e);
            throw new RuntimeException("支付请求失败！！！");
        }
    }

    public String query(String outTradNo, String tradeNo) {
        try {
            // 初始化SDK
            AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig());

            // 构造请求参数以调用接口
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            AlipayTradeQueryModel model = new AlipayTradeQueryModel();

            // 设置订单支付时传入的商户订单号
            model.setOutTradeNo(outTradNo);

            // 设置支付宝交易号
            model.setTradeNo(tradeNo);

            // 设置查询选项
            List<String> queryOptions = new ArrayList<String>();
            queryOptions.add("trade_settle_info");
            model.setQueryOptions(queryOptions);

            request.setBizModel(model);
            // 第三方代调用模式下请设置app_auth_token
            // request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");

            AlipayTradeQueryResponse response = alipayClient.execute(request);
            System.err.println("response = " + JSON.toJSONString(response));
            System.out.println(response.getBody());

            if (response.isSuccess()) {
                System.out.println("调用成功");
                return response.getBody();
            } else {
                System.out.println("调用失败");
                // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
                // String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
                // System.out.println(diagnosisUrl);
            }
        } catch (AlipayApiException e) {
            log.error("获取支付宝签名失败！！！", e);
            throw new RuntimeException("获取支付宝签名失败！！！");
        }
        return "";
    }
}
