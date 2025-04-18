package com.lz.config.manager.sms.model;

import lombok.Data;

import java.util.Map;

/**
 * Project: Picture
 * Package: com.lz.common.manage.sms.model
 * Author: YY
 * CreateTime: 2025-03-19  11:35
 * Description: SmsBody
 * Version: 1.0
 */
@Data
public class SmsBody {
    private String phoneNumbers;
    private String signName;
    private String templateCode;
    private Map<String, String> templateParams;
}
