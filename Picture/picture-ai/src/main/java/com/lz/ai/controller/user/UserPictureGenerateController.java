package com.lz.ai.controller.user;

import com.lz.ai.model.dto.generateLogInfo.UserGenerateLogInfoRequest;
import com.lz.ai.service.IGenerateLogInfoService;
import com.lz.ai.strategy.generate.domain.AiGenerateRequest;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/generate")
    @PreAuthorize("@uss.hasLogin()")
    public AjaxResult add(@Validated @RequestBody AiGenerateRequest request) {
        request.setUserId(getUserId());
        request.setUsername(getUsername());
        DeviceInfo deviceInfo = IpUtils.getDeviceInfo();
        BeanUtils.copyProperties(deviceInfo, request);
        return success(generateLogInfoService.userGenerate(request));
    }

    @GetMapping("/query/{logId}")
    @PreAuthorize("@uss.hasLogin()")
    public AjaxResult getInfo(@PathVariable("logId") String logId) {
        return success(generateLogInfoService.queryTask(logId,getUserId(),getUsername()));
    }

    @GetMapping("/list")
    @PreAuthorize("@uss.hasLogin()")
    public TableDataInfo list(UserGenerateLogInfoRequest request) {
        request.setUserId(getUserId());
        return generateLogInfoService.userSelectGenerateLogInfoList(request);
    }
}
