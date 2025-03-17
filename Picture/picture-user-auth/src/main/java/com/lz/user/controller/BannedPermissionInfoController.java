package com.lz.user.controller;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.enums.BusinessType;
import com.lz.user.model.domain.BannedPermissionInfo;
import com.lz.user.model.vo.bannedPermissionInfo.BannedPermissionInfoVo;
import com.lz.user.model.dto.bannedPermissionInfo.BannedPermissionInfoQuery;
import com.lz.user.model.dto.bannedPermissionInfo.BannedPermissionInfoInsert;
import com.lz.user.model.dto.bannedPermissionInfo.BannedPermissionInfoEdit;
import com.lz.user.service.IBannedPermissionInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 用户封禁权限Controller
 *
 * @author YY
 * @date 2025-03-17
 */
@RestController
@RequestMapping("/user/auth/bannedPermissionInfo")
public class BannedPermissionInfoController extends BaseController
{
    @Resource
    private IBannedPermissionInfoService bannedPermissionInfoService;

    /**
     * 查询用户封禁权限列表
     */
    @PreAuthorize("@ss.hasPermi('user:bannedPermissionInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(BannedPermissionInfoQuery bannedPermissionInfoQuery)
    {
        BannedPermissionInfo bannedPermissionInfo = BannedPermissionInfoQuery.queryToObj(bannedPermissionInfoQuery);
        startPage();
        List<BannedPermissionInfo> list = bannedPermissionInfoService.selectBannedPermissionInfoList(bannedPermissionInfo);
        List<BannedPermissionInfoVo> listVo= list.stream().map(BannedPermissionInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户封禁权限列表
     */
    @PreAuthorize("@ss.hasPermi('user:bannedPermissionInfo:export')")
    @Log(title = "用户封禁权限", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BannedPermissionInfoQuery bannedPermissionInfoQuery)
    {
        BannedPermissionInfo bannedPermissionInfo = BannedPermissionInfoQuery.queryToObj(bannedPermissionInfoQuery);
        List<BannedPermissionInfo> list = bannedPermissionInfoService.selectBannedPermissionInfoList(bannedPermissionInfo);
        ExcelUtil<BannedPermissionInfo> util = new ExcelUtil<BannedPermissionInfo>(BannedPermissionInfo.class);
        util.exportExcel(response, list, "用户封禁权限数据");
    }

    /**
     * 获取用户封禁权限详细信息
     */
    @PreAuthorize("@ss.hasPermi('user:bannedPermissionInfo:query')")
    @GetMapping(value = "/{bannedId}")
    public AjaxResult getInfo(@PathVariable("bannedId") String bannedId)
    {
        BannedPermissionInfo bannedPermissionInfo = bannedPermissionInfoService.selectBannedPermissionInfoByBannedId(bannedId);
        return success(BannedPermissionInfoVo.objToVo(bannedPermissionInfo));
    }

    /**
     * 新增用户封禁权限
     */
    @PreAuthorize("@ss.hasPermi('user:bannedPermissionInfo:add')")
    @Log(title = "用户封禁权限", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BannedPermissionInfoInsert bannedPermissionInfoInsert)
    {
        BannedPermissionInfo bannedPermissionInfo = BannedPermissionInfoInsert.insertToObj(bannedPermissionInfoInsert);
        return toAjax(bannedPermissionInfoService.insertBannedPermissionInfo(bannedPermissionInfo));
    }

    /**
     * 修改用户封禁权限
     */
    @PreAuthorize("@ss.hasPermi('user:bannedPermissionInfo:edit')")
    @Log(title = "用户封禁权限", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BannedPermissionInfoEdit bannedPermissionInfoEdit)
    {
        BannedPermissionInfo bannedPermissionInfo = BannedPermissionInfoEdit.editToObj(bannedPermissionInfoEdit);
        return toAjax(bannedPermissionInfoService.updateBannedPermissionInfo(bannedPermissionInfo));
    }

    /**
     * 删除用户封禁权限
     */
    @PreAuthorize("@ss.hasPermi('user:bannedPermissionInfo:remove')")
    @Log(title = "用户封禁权限", businessType = BusinessType.DELETE)
    @DeleteMapping("/{bannedIds}")
    public AjaxResult remove(@PathVariable String[] bannedIds)
    {
        return toAjax(bannedPermissionInfoService.deleteBannedPermissionInfoByBannedIds(bannedIds));
    }
}
