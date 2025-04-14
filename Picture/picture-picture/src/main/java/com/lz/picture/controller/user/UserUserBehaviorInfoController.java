package com.lz.picture.controller.user;

import com.lz.common.core.domain.AjaxResult;
import com.lz.picture.model.domain.UserBehaviorInfo;
import com.lz.picture.model.dto.userBehaviorInfo.UserBehaviorInfoAdd;
import com.lz.picture.service.IUserBehaviorInfoService;
import com.lz.user.controller.admin.UserInfoController;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户行为Controller
 *
 * @author YY
 * @date 2025-04-12
 */
@RestController
@RequestMapping("/user/picture/userBehaviorInfo")
public class UserUserBehaviorInfoController extends BaseUserInfoController {
    @Resource
    private IUserBehaviorInfoService userBehaviorInfoService;

    /**
     * 新增用户行为
     */
    @PreAuthorize("@uss.hasPermi('picture:userBehaviorInfo:add')")
    @PostMapping
    public AjaxResult add(@RequestBody @Validated UserBehaviorInfoAdd userBehaviorInfoInsert) {
        UserBehaviorInfo userBehaviorInfo = UserBehaviorInfoAdd.addToObj(userBehaviorInfoInsert);
        userBehaviorInfo.setUserId(getUserId());
        return success(userBehaviorInfoService.userInsertUserBehaviorInfo(userBehaviorInfo));
    }
}
