package com.lz.config.controller;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
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
import com.lz.config.model.domain.PermissionInfo;
import com.lz.config.model.vo.permissionInfo.PermissionInfoVo;
import com.lz.config.model.dto.permissionInfo.PermissionInfoQuery;
import com.lz.config.model.dto.permissionInfo.PermissionInfoInsert;
import com.lz.config.model.dto.permissionInfo.PermissionInfoEdit;
import com.lz.config.service.IPermissionInfoService;
import com.lz.common.utils.poi.ExcelUtil;

/**
 * 权限信息Controller
 *
 * @author ruoyi
 * @date 2025-02-28
 */
@RestController
@RequestMapping("/config/permissionInfo")
public class PermissionInfoController extends BaseController {
    @Resource
    private IPermissionInfoService permissionInfoService;

    /**
     * 查询权限信息列表
     */
    @PreAuthorize("@ss.hasPermi('config:permissionInfo:list')")
    @GetMapping("/list")
    public AjaxResult list(PermissionInfoQuery permissionInfoQuery) {
        PermissionInfo permissionInfo = PermissionInfoQuery.queryToObj(permissionInfoQuery);
//        List<PermissionInfo> list = permissionInfoService.selectPermissionInfoList(permissionInfo);
        List<PermissionInfo> list = permissionInfoService.list(permissionInfoService.getQueryWrapper(permissionInfoQuery));
        List<PermissionInfoVo> listVo = list.stream().map(PermissionInfoVo::objToVo).collect(Collectors.toList());
        return success(listVo);
    }

    /**
     * 导出权限信息列表
     */
    @PreAuthorize("@ss.hasPermi('config:permissionInfo:export')")
    @Log(title = "权限信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PermissionInfoQuery permissionInfoQuery) {
        PermissionInfo permissionInfo = PermissionInfoQuery.queryToObj(permissionInfoQuery);
        List<PermissionInfo> list = permissionInfoService.selectPermissionInfoList(permissionInfo);
        ExcelUtil<PermissionInfo> util = new ExcelUtil<PermissionInfo>(PermissionInfo.class);
        util.exportExcel(response, list, "权限信息数据");
    }

    /**
     * 获取权限信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('config:permissionInfo:query')")
    @GetMapping(value = "/{permissionId}")
    public AjaxResult getInfo(@PathVariable("permissionId") Long permissionId) {
//        PermissionInfo permissionInfo = permissionInfoService.selectPermissionInfoByPermissionId(permissionId);
        PermissionInfo permissionInfo = permissionInfoService.getById(permissionId);
        return success(PermissionInfoVo.objToVo(permissionInfo));
    }

    /**
     * 新增权限信息
     */
    @PreAuthorize("@ss.hasPermi('config:permissionInfo:add')")
    @Log(title = "权限信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Validated PermissionInfoInsert permissionInfoInsert) {
        PermissionInfo permissionInfo = PermissionInfoInsert.insertToObj(permissionInfoInsert);
        return toAjax(permissionInfoService.insertPermissionInfo(permissionInfo));
    }

    /**
     * 修改权限信息
     */
    @PreAuthorize("@ss.hasPermi('config:permissionInfo:edit')")
    @Log(title = "权限信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody @Validated PermissionInfoEdit permissionInfoEdit) {
        PermissionInfo permissionInfo = PermissionInfoEdit.editToObj(permissionInfoEdit);
        return toAjax(permissionInfoService.updatePermissionInfo(permissionInfo));
    }

    /**
     * 删除权限信息
     */
    @PreAuthorize("@ss.hasPermi('config:permissionInfo:remove')")
    @Log(title = "权限信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{permissionIds}")
    public AjaxResult remove(@PathVariable Long[] permissionIds) {
        return toAjax(permissionInfoService.deletePermissionInfoByPermissionIds(permissionIds));
    }
}
