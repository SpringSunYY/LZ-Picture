package com.lz.common.config;

import com.lz.common.factory.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
}
