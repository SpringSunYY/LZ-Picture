package com.lz.common.config;

import com.lz.common.constant.Constants;
import com.lz.common.utils.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Project: Picture
 * Package: com.lz.common.config
 * Author: YY
 * CreateTime: 2025-03-27  09:38
 * Description: OssConfig
 * Version: 1.0
 */
@Component
@PropertySource(value = {"classpath:application-config.yml"})
@ConfigurationProperties(prefix = "aliyun")
@Data
public class OssConfig {
    private String accessKeyId;
    private String accessKeySecret;
    private String bucket;
    private String dir;
    private String endpoint;
    private String region;
    private String dnsUrl;

    private static OssConfig staticConfig;

    @PostConstruct
    public void init() {
        staticConfig = this;
    }

    public static String getDnsUrl() {
        return staticConfig.dnsUrl;
    }


    /**
     * 构建url
     *
     * @param url    地址
     * @param dnsUrl 域名
     * @return
     */
    public static String builderUrl(String url, String dnsUrl) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        if (StringUtils.isNotEmpty(dnsUrl)) {
            return dnsUrl + url;
        } else {
            return getDnsUrl() + url;
        }
    }

    /**
     * 构建url
     *
     * @param url 地址
     * @return
     */
    public static String builderUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        //判断是否以http://开头
        if (url.startsWith(Constants.HTTP_PREFIX)) {
            return url;
        } else {
            return getDnsUrl() + url;
        }
    }
}
