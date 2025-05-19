package com.lz.points.config;

import com.lz.common.factory.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 阿里云支付配置
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-14  21:24
 * @Version: 1.0
 */
@Data
@Configuration
@PropertySource(
        value = {"classpath:application-config.yml"},
        factory = YamlPropertySourceFactory.class)
public class AlipayPaymentConfig {
    /**
     * 支付宝私钥
     */
    @Value("${alipay.privateKey}")
    private String privateKey;
    /**
     * 支付宝公钥
     */
    @Value("${alipay.publicKey}")
    private String publicKey;
    /**
     * 服务URL
     */
    @Value("${alipay.serverUrl}")
    private String serverUrl;
    /**
     * APPID
     */
    @Value("${alipay.appId}")
    private String appId;
    /**
     * 异步回调地址
     */
    @Value("${alipay.notifyUrl}")
    private String notifyUrl;
    /**
     * 同步回调地址
     */
    @Value("${alipay.returnUrl}")
    private String returnUrl;
    /**
     * 重定向地址
     */
    @Value("${alipay.redirectUrl}")
    private String redirectUrl;
    /**
     * 格式
     */
    @Value("${alipay.format}")
    private String format;
    /**
     * 编码
     */
    @Value("${alipay.charset}")
    private String charset;
    /**
     * 签名类型
     */
    @Value("${alipay.signType}")
    private String signType;
    /**
     * 产品编码
     */
    @Value("${alipay.productCode}")
    private String productCode;
    /**
     * 超时时间
     */
    @Value("${alipay.timeoutExpress}")
    private String timeoutExpress;
    /**
     * 连接超时时间
     */
    @Value("${alipay.connectTimeout}")
    private Integer connectTimeout;
    /**
     * 读取超时
     */
    @Value("${alipay.readTimeout}")
    private Integer readTimeout;
}
