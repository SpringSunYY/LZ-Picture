package com.lz.config.controller.admin;

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
import com.lz.config.model.domain.MenuInfo;
import com.lz.config.model.vo.menuInfo.MenuInfoVo;
import com.lz.config.model.dto.menuInfo.MenuInfoQuery;
import com.lz.config.model.dto.menuInfo.MenuInfoInsert;
import com.lz.config.model.dto.menuInfo.MenuInfoEdit;
import com.lz.config.service.IMenuInfoService;
import com.lz.common.utils.poi.ExcelUtil;

/**
 * 菜单信息Controller
 *
 * @author YY
 * @date 2025-03-30
 */
@RestController
@RequestMapping("/admin/config/menuInfo")
public class MenuInfoController extends BaseController {
    @Resource
    private IMenuInfoService menuInfoService;

    /**
     * 查询菜单信息列表
     */
    @PreAuthorize("@ss.hasPermi('config:menuInfo:list')")
    @GetMapping("/list")
    public AjaxResult list(MenuInfoQuery menuInfoQuery) {
        MenuInfo menuInfo = MenuInfoQuery.queryToObj(menuInfoQuery);
        List<MenuInfo> list = menuInfoService.selectMenuInfoList(menuInfo);
        List<MenuInfoVo> listVo = list.stream().map(MenuInfoVo::objToVo).collect(Collectors.toList());
        return success(listVo);
    }

    /**
     * 导出菜单信息列表
     */
    @PreAuthorize("@ss.hasPermi('config:menuInfo:export')")
    @Log(title = "菜单信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MenuInfoQuery menuInfoQuery) {
        MenuInfo menuInfo = MenuInfoQuery.queryToObj(menuInfoQuery);
        List<MenuInfo> list = menuInfoService.selectMenuInfoList(menuInfo);
        ExcelUtil<MenuInfo> util = new ExcelUtil<MenuInfo>(MenuInfo.class);
        util.exportExcel(response, list, "菜单信息数据");
    }

    /**
     * 获取菜单信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('config:menuInfo:query')")
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable("menuId") Long menuId) {
        MenuInfo menuInfo = menuInfoService.selectMenuInfoByMenuId(menuId);
        return success(MenuInfoVo.objToVo(menuInfo));
    }

    /**
     * 新增菜单信息
     */
    @PreAuthorize("@ss.hasPermi('config:menuInfo:add')")
    @Log(title = "菜单信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MenuInfoInsert menuInfoInsert) {
        MenuInfo menuInfo = MenuInfoInsert.insertToObj(menuInfoInsert);
        return toAjax(menuInfoService.insertMenuInfo(menuInfo));
    }

    @PreAuthorize("@ss.hasPermi('config:menuInfo:add')")
    @DeleteMapping("/reset")
    public AjaxResult reset() {
        return toAjax(menuInfoService.initMenuInfoCache());
    }

    /**
     * 修改菜单信息
     */
    @PreAuthorize("@ss.hasPermi('config:menuInfo:edit')")
    @Log(title = "菜单信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MenuInfoEdit menuInfoEdit) {
        MenuInfo menuInfo = MenuInfoEdit.editToObj(menuInfoEdit);
        return toAjax(menuInfoService.updateMenuInfo(menuInfo));
    }

    /**
     * 删除菜单信息
     */
    @PreAuthorize("@ss.hasPermi('config:menuInfo:remove')")
    @Log(title = "菜单信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuIds}")
    public AjaxResult remove(@PathVariable Long[] menuIds) {
        return toAjax(menuInfoService.deleteMenuInfoByMenuIds(menuIds));
    }
}
