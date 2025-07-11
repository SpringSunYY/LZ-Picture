package com.lz.picture.controller.user;

import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.picture.model.domain.SpaceMemberInfo;
import com.lz.picture.model.dto.spaceMemberInfo.*;
import com.lz.picture.model.vo.spaceMemberInfo.SpaceMemberInfoVo;
import com.lz.picture.service.ISpaceMemberInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 空间成员信息Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/user/picture/spaceMemberInfo")
public class UserSpaceMemberInfoController extends BaseUserInfoController
{
    @Resource
    private ISpaceMemberInfoService spaceMemberInfoService;

    /**
     * 查询空间成员信息列表
     */
    @PreAuthorize("@uss.hasPermi('space:member')")
    @GetMapping("/list")
    public TableDataInfo list(UserSpaceMemberInfoQuery query)
    {
        if (StringUtils.isNull(query.getPageSize())) {
            query.setPageSize(50);
        }
        if (query.getPageSize() > 50) {
            query.setPageSize(50);
        }
        query.setUserId(getUserId());
        return spaceMemberInfoService.listUserSpaceMemberInfoList(query);
    }

    /**
     * 删除空间成员
     */
    @PreAuthorize("@uss.hasPermi('space:member')")
    @DeleteMapping("/{memberId}")
    public AjaxResult remove(@RequestBody @PathVariable("memberId") String memberId)
    {
        return toAjax(spaceMemberInfoService.userDeleteSpaceMemberInfoByMemberId(memberId));
    }

    /**
     * 更新团队空间用户角色
     */
    @PreAuthorize("@uss.hasPermi('space:member')")
    @PutMapping()
    public AjaxResult update(@RequestBody @Validated SpaceMemberInfoUpdate spaceMemberInfoUpdate){
        SpaceMemberInfo spaceMemberInfo = SpaceMemberInfoUpdate.editToObj(spaceMemberInfoUpdate);
        spaceMemberInfo.setUserId(getUserId());
        return toAjax(spaceMemberInfoService.userUpdateSpaceMemberInfo(spaceMemberInfo));
    }
}
