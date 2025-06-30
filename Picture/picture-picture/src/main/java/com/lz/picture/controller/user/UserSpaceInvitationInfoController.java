package com.lz.picture.controller.user;

import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.utils.StringUtils;
import com.lz.picture.model.domain.SpaceInvitationInfo;
import com.lz.picture.model.dto.spaceInvitationInfo.SpaceInvitationInfoAdd;
import com.lz.picture.model.dto.spaceInvitationInfo.UserSpaceInvitationInfoAction;
import com.lz.picture.model.dto.spaceInvitationInfo.UserSpaceInvitationInfoQuery;
import com.lz.picture.service.ISpaceInvitationInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("@uss.hasPermi('space:invitation')")
    @GetMapping("/list")
    public TableDataInfo list(UserSpaceInvitationInfoQuery userSpaceInvitationInfoQuery) {
        if (StringUtils.isNull(userSpaceInvitationInfoQuery.getPageSize())) {
            userSpaceInvitationInfoQuery.setPageSize(50);
        }
        if (userSpaceInvitationInfoQuery.getPageSize() > 50) {
            userSpaceInvitationInfoQuery.setPageSize(50);
        }
        if (StringUtils.isNotEmpty(userSpaceInvitationInfoQuery.getUserType()) && userSpaceInvitationInfoQuery.getUserType().equals("0")) {
            userSpaceInvitationInfoQuery.setInvitationUserId(getUserId());
        } else {
            userSpaceInvitationInfoQuery.setUserId(getUserId());
        }
        return spaceInvitationInfoService.listUserSpaceInvitationInfoTable(userSpaceInvitationInfoQuery);
    }

    /**
     * 用户空间成员邀请操作
     */
    @PreAuthorize("@uss.hasPermi('space:invitation')")
    @PutMapping("/action")
    public AjaxResult action(@RequestBody @Validated UserSpaceInvitationInfoAction userSpaceInvitationInfoAction) {
        SpaceInvitationInfo spaceInvitationInfo = UserSpaceInvitationInfoAction.dtoToObj(userSpaceInvitationInfoAction);
        spaceInvitationInfo.setUserId(getUserId());
        return toAjax(spaceInvitationInfoService.userActionSpaceInvitationInfo(spaceInvitationInfo));
    }

    /**
     * 邀请用户空间成员取消操作
     */
    @PreAuthorize("@uss.hasPermi('space:invitation')")
    @PutMapping("/cancel")
    public AjaxResult cancel(@RequestBody @Validated UserSpaceInvitationInfoAction userSpaceInvitationInfoAction) {
        return toAjax(spaceInvitationInfoService.userCancelSpaceInvitationInfoByInvitationId(userSpaceInvitationInfoAction.getInvitationId(), getUserId()));
    }

    /**
     * 删除空间成员邀请记录
     */
    @PreAuthorize("@uss.hasPermi('space:invitation')")
    @DeleteMapping("/{invitationId}")
    public AjaxResult remove(@PathVariable("invitationId") String invitationId) {
        return toAjax(spaceInvitationInfoService.userDeleteSpaceInvitationInfoByInvitationId(invitationId, getUserId()));
    }
}
