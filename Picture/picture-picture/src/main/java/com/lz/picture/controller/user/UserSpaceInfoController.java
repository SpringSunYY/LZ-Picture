package com.lz.picture.controller.user;

import com.lz.common.config.OssConfig;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.StringUtils;
import com.lz.config.service.IConfigInfoService;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.dto.spaceInfo.*;
import com.lz.picture.model.vo.spaceInfo.UserSpaceInfoVo;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.picture.utils.SpaceAuthUtils;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * Project: Picture
 * Package: com.lz.picture.controller.user
 * Author: YY
 * CreateTime: 2025-03-30  21:49
 * Description: UserSpaceInfoController
 * Version: 1.0
 */
@RestController
@RequestMapping("/user/picture/spaceInfo")
public class UserSpaceInfoController extends BaseUserInfoController {
    @Resource
    private ISpaceInfoService spaceInfoService;

    @Resource
    private SpaceAuthUtils spaceAuthUtils;

    @PreAuthorize("@uss.hasPermi('picture:space:add')")
    @PostMapping
    public AjaxResult add(@RequestBody @Validated SpaceInfoAdd spaceInfoAdd) {
        SpaceInfo spaceInfo = SpaceInfoAdd.addToObj(spaceInfoAdd);
        String userId = getLoginUser().getUserId();
        spaceInfo.setUserId(userId);
        return toAjax(spaceInfoService.userInsertSpaceInfo(spaceInfo));
    }

    @PreAuthorize("@uss.hasPermi('picture:space:update')")
    @PutMapping
    public AjaxResult update(@RequestBody @Validated SpaceInfoUpdate spaceInfoUpdate) {
        SpaceInfo spaceInfo = SpaceInfoUpdate.updateToObj(spaceInfoUpdate);
        String userId = getLoginUser().getUserId();
        spaceInfo.setUserId(userId);
        return toAjax(spaceInfoService.userUpdateSpaceInfo(spaceInfo));
    }

    @PreAuthorize("@uss.hasPermi('picture:space')")
    @GetMapping("/mySpace")
    public TableDataInfo mySpace(SpaceInfoQuery spaceInfoQuery) {
        spaceInfoQuery.setUserId(getUserId());
        return spaceInfoService.mySpace(spaceInfoQuery);
    }

    @PreAuthorize("@uss.hasPermi('space:manage:personal:table')")
    @GetMapping("/list/table")
    public TableDataInfo listTable(UserSpaceInfoQuery userSpaceInfoQuery) {
        if (StringUtils.isNull(userSpaceInfoQuery.getPageSize())) {
            userSpaceInfoQuery.setPageSize(50);
        }
        if (userSpaceInfoQuery.getPageSize() > 50) {
            userSpaceInfoQuery.setPageSize(50);
        }
        userSpaceInfoQuery.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
        userSpaceInfoQuery.setUserId(getUserId());
        return spaceInfoService.listSpaceInfoTable(userSpaceInfoQuery);
    }

    @PreAuthorize("@uss.hasPermi('space:manage:team:table')")
    @GetMapping("/list/team/table")
    public TableDataInfo listTeamTable(UserTeamSpaceInfoQuery userTeamSpaceInfoQuery) {
        if (StringUtils.isNull(userTeamSpaceInfoQuery.getPageSize())) {
            userTeamSpaceInfoQuery.setPageSize(50);
        }
        if (userTeamSpaceInfoQuery.getPageSize() > 50) {
            userTeamSpaceInfoQuery.setPageSize(50);
        }
        userTeamSpaceInfoQuery.setUserId(getUserId());
        return spaceInfoService.listTeamSpaceInfoTable(userTeamSpaceInfoQuery);
    }

    @PreAuthorize("@uss.hasPermi('picture:space')")
    @GetMapping("/{spaceId}")
    public AjaxResult getInfo(@PathVariable("spaceId") String spaceId) {
        SpaceInfo spaceInfo = spaceInfoService.selectSpaceInfoBySpaceId(spaceId);
        return success(UserSpaceInfoVo.objToVo(spaceInfo));
    }

    @PreAuthorize("@uss.hasPermi('space:member')")
    @GetMapping("/perm")
    public AjaxResult getPerm() {
        return success(spaceAuthUtils.getSpaceMemberPerm());
    }
}
