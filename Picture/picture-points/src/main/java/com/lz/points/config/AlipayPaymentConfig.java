package com.lz.points.config;

import com.lz.common.factory.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConfigurationProperties(prefix = "alipay")
public class AlipayPaymentConfig {
    /**
     * 支付宝私钥
     */
    private String privateKey;
    /**
     * 支付宝公钥
     */
    private String publicKey;
    /**
     * 服务URL
     */
    private String serverUrl;
    /**
     * APPID
     */
    private String appId;
    /**
     * 异步回调地址
     */
    private String notifyUrl;
    /**
     * 同步回调地址
     */
    private String returnUrl;
    /**
     * 重定向地址
     */
    private String redirectUrl;
    /**
     * 格式
     */
    private String format;
    /**
     * 编码
     */
    private String charset;
    /**
     * 签名类型
     */
    private String signType;
    /**
     * 产品编码
     */
    private String productCode;
    /**
     * 超时时间
     */
    private String timeoutExpress;
    /**
     * 连接超时时间
     */
    private Integer connectTimeout;
    /**
     * 读取超时
     */
    private Integer readTimeout;
}
