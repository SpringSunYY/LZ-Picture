package com.lz.config;

import com.alibaba.fastjson2.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.lz.config.manager.sms.SmsManager;
import com.lz.config.manager.sms.model.LoginCode;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Project: Picture
 * Package: com.lz.config
 * Author: YY
 * CreateTime: 2025-04-18  21:35
 * Description: SmsTest
 * Version: 1.0
 */
@SpringBootTest
public class SmsTest {
    @Resource
    private SmsManager smsManager;

    @Test
    public void sendSms() {
        LoginCode loginCode = new LoginCode();
        loginCode.setCode("123456");
        String jsonString = JSONObject.toJSONString(loginCode);
        SendSmsResponse response = smsManager.sendSms("18585595238",
                "荔枝开发阶段短信服务",
                "SMS_480850068", jsonString
        );
    }
}
