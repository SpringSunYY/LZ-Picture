package com.lz.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.lz.config.manager.sms.SmsManager;
import com.lz.config.manager.sms.SmsTemplate;
import com.lz.config.manager.sms.model.SmsBody;
import com.lz.config.manager.sms.model.SmsResponse;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

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

    @Resource
    private SmsTemplate smsTemplate;

    @Test
    public void sendSms() {
        SmsBody smsBody = new SmsBody();
        smsBody.setPhoneNumbers("18585595238");
        smsBody.setSignName("荔枝开发阶段短信服务");
        smsBody.setTemplateCode("SMS_480850068");

        smsBody.setTemplateParams(new HashMap<>());


        String jsonString = JSONObject.toJSONString(smsBody);
        SendSmsResponse response = smsManager.sendSms("18585595238",
                "荔枝开发阶段短信服务",
                "SMS_480850068", jsonString
        );
    }

    public static void main(String[] args) {
        String s = "{\n" +
                "    \"userName\":\"YY\",\n" +
                "    \"to\":\"XC\"\n" +
                "}";
        HashMap<String, String> hashMap = JSON.parseObject(s, HashMap.class);
        hashMap.put("userName", "XC");
        System.out.println("hashMap = " + hashMap);
    }

    @Test
    public void sendCode() {
        String code = "123456";
        String phone = "18585595238";
        String locale = "zh";
        SmsResponse smsResponse = smsTemplate.sendCode(code, phone, locale);
        System.out.println("smsResponse = " + smsResponse);
    }
}
