package com.lz.picture.controller.user;

import cn.hutool.core.util.StrUtil;
import com.lz.ai.api.aliyunai.AliYunAiApi;
import com.lz.ai.api.aliyunai.model.CreateOutPaintingTaskResponse;
import com.lz.ai.api.aliyunai.model.GetOutPaintingTaskResponse;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.domain.model.LoginUserInfo;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.picture.model.dto.pictureInfo.CreatePictureOutPaintingTaskRequest;
import com.lz.picture.service.IAiPictureService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Project: Picture
 * Package: com.lz.picture.controller.user
 * Author: YY
 * CreateTime: 2025-04-23  17:07
 * Description: UserAiEditPictureController
 * Version: 1.0
 */
@RestController
@RequestMapping("/user/picture/ai")
public class UserAiEditPictureController extends BaseUserInfoController {
    @Resource
    IAiPictureService aiPictureService;

    @Resource
    AliYunAiApi aliYunAiApi;

    /**
     * 创建 AI 扩图任务
     */
    @PreAuthorize("@uss.hasPermi('picture:ai:outPainting:createTask')")
    @PostMapping("/outPainting/createTask")
    public AjaxResult createPictureOutPaintingTask(@RequestBody CreatePictureOutPaintingTaskRequest createPictureOutPaintingTaskRequest,
                                                   HttpServletRequest request) {
        ThrowUtils.throwIf(StringUtils.isNull(createPictureOutPaintingTaskRequest)
                        || StringUtils.isNull(createPictureOutPaintingTaskRequest.getPictureId()),
                "参数异常！！！");
        LoginUserInfo loginUser = getLoginUser();
        CreateOutPaintingTaskResponse response = aiPictureService.createPictureOutPaintingTask(createPictureOutPaintingTaskRequest, loginUser);
        return success(response);
    }

    /**
     * 查询 AI 扩图任务
     */
    @PreAuthorize("@uss.hasPermi('picture:ai:outPainting:createTask')")
    @GetMapping("/outPainting/getTask")
    public AjaxResult getPictureOutPaintingTask(String taskId) {
        ThrowUtils.throwIf(StrUtil.isBlank(taskId), "请求参数错误");
        GetOutPaintingTaskResponse task = aliYunAiApi.getOutPaintingTask(taskId);
        return success(task);
    }
}
