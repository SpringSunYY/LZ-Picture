package com.lz.config.manager.sms.model;

import lombok.Data;

/**
 * Project: Picture
 * Package: com.lz.config.manager.sms.model
 * Author: YY
 * CreateTime: 2025-04-18  23:33
 * Description: SmsResponse
 * Version: 1.0
 */
@Data
public class SmsResponse {
    private String code;
    private String message;
    private String requestId;
    private String bizId;
}
