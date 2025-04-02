package com.lz.picture.controller.user;

import com.lz.common.annotation.Log;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.dto.spaceInfo.SpaceInfoAdd;
import com.lz.picture.model.dto.spaceInfo.SpaceInfoInsert;
import com.lz.picture.model.dto.spaceInfo.SpaceInfoQuery;
import com.lz.picture.model.vo.spaceInfo.UserSpaceInfoVo;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import com.sun.jna.platform.win32.WinNT;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public AjaxResult add(@RequestBody SpaceInfoAdd spaceInfoAdd) {
        SpaceInfo spaceInfo = SpaceInfoAdd.addToObj(spaceInfoAdd);
        String userId = getLoginUser().getUserId();
        spaceInfo.setUserId(userId);
        return toAjax(spaceInfoService.userInsertSpaceInfo(spaceInfo));
    }

    @PreAuthorize("@uss.hasPermi('picture:space')")
    @GetMapping("/mySpace")
    public TableDataInfo mySpace(SpaceInfoQuery spaceInfoQuery) {
        //无需分页只需要拿到自己所有的空间即可 TODO后续还有自己加入的空间
        SpaceInfo spaceInfo = SpaceInfoQuery.queryToObj(spaceInfoQuery);
        spaceInfo.setUserId(getLoginUser().getUserId());
        spaceInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
        List<SpaceInfo> list = spaceInfoService.selectSpaceInfoList(spaceInfo);
        //转为Vo
        List<UserSpaceInfoVo> listVo = list.stream().map(UserSpaceInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo dataTable = getDataTable(list);
        dataTable.setTotal(list.size());
        dataTable.setRows(listVo);
        return dataTable;
    }
}
