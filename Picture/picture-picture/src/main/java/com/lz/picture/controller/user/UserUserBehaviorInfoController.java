package com.lz.picture.controller.user;

import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.picture.model.domain.UserBehaviorInfo;
import com.lz.picture.model.dto.userBehaviorInfo.UserBehaviorInfoAdd;
import com.lz.picture.service.IUserBehaviorInfoService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户行为Controller
 *
 * @author YY
 * @date 2025-04-12
 */
@RestController
@RequestMapping("/user/picture/userBehaviorInfo")
public class UserUserBehaviorInfoController extends BaseController {
    @Resource
    private IUserBehaviorInfoService userBehaviorInfoService;

    /**
     * 新增用户行为
     */
    @PreAuthorize("@uss.hasPermi('picture:userBehaviorInfo:add')")
    @PostMapping
    public AjaxResult add(@RequestBody @Validated UserBehaviorInfoAdd userBehaviorInfoInsert) {
        UserBehaviorInfo userBehaviorInfo = UserBehaviorInfoAdd.addToObj(userBehaviorInfoInsert);
        return toAjax(userBehaviorInfoService.userInsertUserBehaviorInfo(userBehaviorInfo));
    }
}
