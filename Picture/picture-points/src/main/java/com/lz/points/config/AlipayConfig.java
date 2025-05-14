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
@PropertySource(value = {"classpath:application-config.yml"}, factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "alipay")
public class AlipayConfig {
    public static String privateKey;
    public static String alipayPublicKey;
    public static String serverUrl;
    public static String appId;
    public static String notifyUrl;
    public static String format;
    public static String charset;
    public static String signType;

    public static com.alipay.api.AlipayConfig getAlipayConfig() {
        com.alipay.api.AlipayConfig alipayConfig = new com.alipay.api.AlipayConfig();
        alipayConfig.setServerUrl(serverUrl);
        alipayConfig.setAppId(appId);
        alipayConfig.setPrivateKey(privateKey);
        alipayConfig.setFormat(format);
        alipayConfig.setAlipayPublicKey(alipayPublicKey);
        alipayConfig.setCharset(charset);
        alipayConfig.setSignType(signType);
        return alipayConfig;
    }
}
