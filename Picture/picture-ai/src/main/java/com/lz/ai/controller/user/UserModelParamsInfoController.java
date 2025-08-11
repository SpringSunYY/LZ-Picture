package com.lz.ai.controller.user;

import com.lz.ai.model.domain.ModelParamsInfo;
import com.lz.ai.model.dto.modelParamsInfo.ModelParamsInfoRequest;
import com.lz.ai.model.vo.modelParamsInfo.ModelParamsInfoVo;
import com.lz.ai.service.IModelParamsInfoService;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AI模型参数配置Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/user/ai/modelParamsInfo")
public class UserModelParamsInfoController extends BaseUserInfoController
{
    @Resource
    private IModelParamsInfoService modelParamsInfoService;

    /**
     * 查询AI模型参数配置列表
     */
    @PreAuthorize("@uss.hasLogin()")
    @GetMapping("/list")
    public AjaxResult list(ModelParamsInfoRequest request)
    {
        return success(modelParamsInfoService.userSelectModelParamsInfoList(request));
    }

}
