package com.lz.picture.controller.user;

import com.lz.common.core.domain.AjaxResult;
import com.lz.picture.model.domain.SpaceInvitationInfo;
import com.lz.picture.model.dto.spaceInvitationInfo.SpaceInvitationInfoAdd;
import com.lz.picture.model.dto.spaceInvitationInfo.SpaceInvitationInfoInsert;
import com.lz.picture.service.ISpaceInvitationInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 空间成员邀请记录Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/user/picture/spaceInvitationInfo")
public class UserSpaceInvitationInfoController extends BaseUserInfoController {
    @Resource
    private ISpaceInvitationInfoService spaceInvitationInfoService;

    @PreAuthorize("@uss.hasPermi('space:invitation')")
    @PostMapping
    public AjaxResult add(@RequestBody @Validated SpaceInvitationInfoAdd spaceInvitationInfoAdd) {
        SpaceInvitationInfo spaceInvitationInfo = SpaceInvitationInfoAdd.insertToObj(spaceInvitationInfoAdd);
        spaceInvitationInfo.setInvitationUserId(getUserId());
        return toAjax(spaceInvitationInfoService.userInsertSpaceInvitationInfo(spaceInvitationInfo));
    }
}
