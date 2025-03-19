package com.lz.common;

import com.alibaba.fastjson2.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.lz.common.manage.sms.SmsManage;
import com.lz.common.manage.sms.model.LoginCode;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Project: Picture
 * Package: com.lz.common
 * Author: YY
 * CreateTime: 2025-03-19  11:41
 * Description: CommonTest
 * Version: 1.0
 */
@SpringBootTest
public class CommonTest {
    @Resource
    private SmsManage smsManage;

    @Test
    public void testSendSms() {
        //TODO 待短信签名审核完成后，再进行测试
        LoginCode loginCode = new LoginCode();
        loginCode.setCode("123456");
        String jsonString = JSONObject.toJSONString(loginCode);
        SendSmsResponse response = smsManage.sendSms("18585595238",
                "荔枝开发阶段短信服务",
                "SMS_480850068", jsonString
        );
        System.out.println("短信接口返回的数据----------------");
        System.out.println("Code=" + response.getCode());
        System.out.println("Message=" + response.getMessage());
        System.out.println("RequestId=" + response.getRequestId());
        System.out.println("BizId=" + response.getBizId());
    }
}
