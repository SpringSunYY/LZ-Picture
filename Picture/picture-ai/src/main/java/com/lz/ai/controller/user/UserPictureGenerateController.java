package com.lz.ai.controller.user;

import com.lz.ai.model.dto.generateLogInfo.GenerateLogInfoRequest;
import com.lz.ai.service.IGenerateLogInfoService;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户生成记录
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-08  23:33
 * @Version: 1.0
 */
@RestController
@RequestMapping("/user/ai/generateLogInfo")
public class UserPictureGenerateController extends BaseUserInfoController {
    @Resource
    private IGenerateLogInfoService generateLogInfoService;

    @PostMapping("/add")
    @PreAuthorize("@uss.hasLogin()")
    public AjaxResult add(@Validated GenerateLogInfoRequest request) {
        request.setUserId(getUserId());
        request.setUsername(getUsername());
        DeviceInfo deviceInfo = IpUtils.getDeviceInfo();
        BeanUtils.copyProperties(deviceInfo, request);
        return success(generateLogInfoService.userGenerate(request));
    }
}
