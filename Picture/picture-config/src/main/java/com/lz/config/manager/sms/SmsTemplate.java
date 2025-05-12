package com.lz.config.manager.sms;

import com.alibaba.fastjson2.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.config.manager.sms.model.SmsResponse;
import com.lz.config.model.domain.InformTemplateInfo;
import com.lz.config.model.enmus.CTemplateTypeEnum;
import com.lz.config.service.IInformTemplateInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Project: Picture
 * Package: com.lz.config.manager.sms
 * Author: YY
 * CreateTime: 2025-04-18  21:41
 * Description: SmsTemplate
 * Version: 1.0
 */
@Service
@Slf4j
public class SmsTemplate {
    @Resource
    private SmsManager smsManager;

    @Resource
    private IInformTemplateInfoService informTemplateInfoService;

    public SmsResponse sendCode(String templateKey,String code, String phone, String locale) {
        InformTemplateInfo templateInfo = informTemplateInfoService.getInformTemplateInfoByKeyAndLocale(templateKey, locale, CTemplateTypeEnum.TEMPLATE_TYPE_1.getValue());
        if (StringUtils.isNull(templateInfo)) {
            log.error("短信模板不存在或者未启用");
        }
        String variables = templateInfo.getVariables();
        // 修改第39行代码
        Map<String, Object> paramsMap = JSONObject.parseObject(variables);
        paramsMap.put("code", code);
        System.out.println("templateInfo = " + templateInfo);
        SendSmsResponse sendSmsResponse = smsManager.sendSms(phone, templateInfo.getServiceSignName(), templateInfo.getServiceTemplateId(), JSONObject.toJSONString(paramsMap));
        SmsResponse smsResponse = new SmsResponse();
        BeanUtils.copyProperties(sendSmsResponse, smsResponse);
        return smsResponse;
    }
}
