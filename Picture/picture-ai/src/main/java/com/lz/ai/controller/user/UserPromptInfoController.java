package com.lz.ai.controller.user;

import com.lz.ai.model.dto.promptInfo.PromptInfoRequest;
import com.lz.ai.service.IPromptInfoService;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.utils.StringUtils;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提示词信息Controller
 *
 * @author YY
 * @date 2025-08-08
 */
@RestController
@RequestMapping("/user/ai/promptInfo")
public class UserPromptInfoController extends BaseUserInfoController {
    @Resource
    private IPromptInfoService promptInfoService;

    /**
     * 查询提示词信息列表
     */
    @PreAuthorize("@uss.hasLogin()")
    @GetMapping("/list")
    public TableDataInfo list(PromptInfoRequest request) {
        if (StringUtils.isNull(request.getPageNum()) || request.getPageNum() < 1) {
            request.setPageNum(1);
        }
        if (StringUtils.isNull(request.getPageSize()) || request.getPageSize() < 0 || request.getPageSize() > 5) {
            request.setPageSize(5);
        }
        return promptInfoService.userSelectPromptInfoList(request);
    }
}
