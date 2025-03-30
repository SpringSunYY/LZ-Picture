package com.lz.picture.controller.user;

import com.lz.common.annotation.Log;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.enums.BusinessType;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.dto.spaceInfo.SpaceInfoAdd;
import com.lz.picture.model.dto.spaceInfo.SpaceInfoInsert;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import com.sun.jna.platform.win32.WinNT;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PreAuthorize("@uss.hasPermi('picture:space:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SpaceInfoAdd spaceInfoAdd)
    {
        SpaceInfo spaceInfo = SpaceInfoAdd.addToObj(spaceInfoAdd);
        String userId = getLoginUser().getUserId();
        spaceInfo.setUserId(userId);
        return toAjax(spaceInfoService.userInsertSpaceInfo(spaceInfo));
    }
}
