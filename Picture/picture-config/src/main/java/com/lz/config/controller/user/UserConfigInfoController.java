package com.lz.config.controller.user;

import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.config.model.vo.configInfo.UserConfigInfoVo;
import com.lz.config.service.IConfigInfoService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配置信息Controller
 *
 * @author YY
 * @date 2025-02-28
 */
@RestController
@RequestMapping("/user/config/configInfo")
public class UserConfigInfoController extends BaseController {
    @Resource
    private IConfigInfoService configInfoService;

    /**
     * 获取配置信息详细信息
     */
    @GetMapping(value = "/key/{configKey}")
    public AjaxResult getInfo(@PathVariable("configKey") String configKey) {
        String configInfoOutCache = configInfoService.getConfigInfoOutCache(configKey);
        UserConfigInfoVo userConfigInfoVo = new UserConfigInfoVo();
        userConfigInfoVo.setConfigValue(configInfoOutCache);
        return success(userConfigInfoVo);
    }
}
