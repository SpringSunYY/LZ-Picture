package com.lz.config.controller.user;

import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.config.model.domain.ConfigInfo;
import com.lz.config.model.dto.configInfo.ConfigInfoEdit;
import com.lz.config.model.dto.configInfo.ConfigInfoInsert;
import com.lz.config.model.dto.configInfo.ConfigInfoQuery;
import com.lz.config.model.vo.configInfo.ConfigInfoVo;
import com.lz.config.model.vo.configInfo.UserConfigInfoVo;
import com.lz.config.service.IConfigInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
