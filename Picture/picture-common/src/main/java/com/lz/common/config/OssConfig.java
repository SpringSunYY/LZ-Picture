package com.lz.common.config;

import com.lz.common.constant.Constants;
import com.lz.common.factory.YamlPropertySourceFactory;
import com.lz.common.utils.StringUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Project: Picture
 * Package: com.lz.common.config
 * Author: YY
 * CreateTime: 2025-03-27  09:38
 * Description: OssConfig
 * Version: 1.0
 */
@Data
@Configuration
@PropertySource(value = {"classpath:application-config.yml"},
        factory = YamlPropertySourceFactory.class)
public class OssConfig {
    @Value("${aliYun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliYun.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliYun.bucket}")
    private String bucket;
    @Value("${aliYun.dir}")
    private String dir;
    @Value("${aliYun.endpoint}")
    private String endpoint;
    @Value("${aliYun.region}")
    private String region;
    @Value("${aliYun.dnsUrl}")
    private String dnsUrl;

    /**
     * 构建url
     *
     * @param url    地址
     * @param dnsUrl 域名
     * @return
     */
    public String builderUrl(String url, String dnsUrl) {
        if (StringUtils.isNotEmpty(dnsUrl)) {
            return dnsUrl + url;
        } else {
            return this.getDnsUrl() + url;
        }
    }

    /**
     * 构建url
     *
     * @param url 地址
     * @return
     */
    public String builderUrl(String url) {
        //判断是否以http://开头
        if (url.startsWith(Constants.HTTP_PREFIX)) {
            return url;
        } else {
            return this.getDnsUrl() + url;
        }
    }
}
