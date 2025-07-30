package com.lz.config.manager.sms;

import cn.hutool.core.util.PhoneUtil;
import com.alibaba.fastjson2.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.lz.common.constant.HttpStatus;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.exception.ServiceException;
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

    @Resource
    private RedisCache redisCache;

    public SmsResponse sendCode(String key, String templateKey, String code, String phone, String locale) {
        if (!PhoneUtil.isPhone(phone)) {
            throw new ServiceException("手机号码格式错误", HttpStatus.BAD_REQUEST);
        }
        //查询缓存是否存在
        if (redisCache.hasKey(key)) {
            throw new ServiceException("验证码已发送，如果未收到，请查看是否被手机拦截，如果存在请输入验证码", HttpStatus.ACCEPTED);
        }
        InformTemplateInfo templateInfo = informTemplateInfoService.getInformTemplateInfoByKeyAndLocale(templateKey, locale, CTemplateTypeEnum.TEMPLATE_TYPE_1.getValue());
        if (StringUtils.isNull(templateInfo)) {
            log.error("短信模板不存在或者未启用");
        }
        String variables = templateInfo.getVariables();
        // 修改第39行代码
        Map<String, Object> paramsMap = JSONObject.parseObject(variables);
        paramsMap.put("code", code);
//        System.out.println("templateInfo = " + templateInfo);
        SendSmsResponse sendSmsResponse = smsManager.sendSms(phone, templateInfo.getServiceSignName(), templateInfo.getServiceTemplateId(), JSONObject.toJSONString(paramsMap));
        SmsResponse smsResponse = new SmsResponse();
        BeanUtils.copyProperties(sendSmsResponse, smsResponse);
        return smsResponse;
    }
}
