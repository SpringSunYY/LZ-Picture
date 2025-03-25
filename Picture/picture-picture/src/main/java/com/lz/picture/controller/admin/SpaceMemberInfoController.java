package com.lz.picture.controller.admin;

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
import com.lz.picture.model.domain.SpaceMemberInfo;
import com.lz.picture.model.vo.spaceMemberInfo.SpaceMemberInfoVo;
import com.lz.picture.model.dto.spaceMemberInfo.SpaceMemberInfoQuery;
import com.lz.picture.model.dto.spaceMemberInfo.SpaceMemberInfoInsert;
import com.lz.picture.model.dto.spaceMemberInfo.SpaceMemberInfoEdit;
import com.lz.picture.service.ISpaceMemberInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 空间成员信息Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/spaceMemberInfo")
public class SpaceMemberInfoController extends BaseController
{
    @Resource
    private ISpaceMemberInfoService spaceMemberInfoService;

    /**
     * 查询空间成员信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceMemberInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpaceMemberInfoQuery spaceMemberInfoQuery)
    {
        SpaceMemberInfo spaceMemberInfo = SpaceMemberInfoQuery.queryToObj(spaceMemberInfoQuery);
        startPage();
        List<SpaceMemberInfo> list = spaceMemberInfoService.selectSpaceMemberInfoList(spaceMemberInfo);
        List<SpaceMemberInfoVo> listVo= list.stream().map(SpaceMemberInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出空间成员信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceMemberInfo:export')")
    @Log(title = "空间成员信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SpaceMemberInfoQuery spaceMemberInfoQuery)
    {
        SpaceMemberInfo spaceMemberInfo = SpaceMemberInfoQuery.queryToObj(spaceMemberInfoQuery);
        List<SpaceMemberInfo> list = spaceMemberInfoService.selectSpaceMemberInfoList(spaceMemberInfo);
        ExcelUtil<SpaceMemberInfo> util = new ExcelUtil<SpaceMemberInfo>(SpaceMemberInfo.class);
        util.exportExcel(response, list, "空间成员信息数据");
    }

    /**
     * 获取空间成员信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceMemberInfo:query')")
    @GetMapping(value = "/{memberId}")
    public AjaxResult getInfo(@PathVariable("memberId") String memberId)
    {
        SpaceMemberInfo spaceMemberInfo = spaceMemberInfoService.selectSpaceMemberInfoByMemberId(memberId);
        return success(SpaceMemberInfoVo.objToVo(spaceMemberInfo));
    }

    /**
     * 新增空间成员信息
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceMemberInfo:add')")
    @Log(title = "空间成员信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SpaceMemberInfoInsert spaceMemberInfoInsert)
    {
        SpaceMemberInfo spaceMemberInfo = SpaceMemberInfoInsert.insertToObj(spaceMemberInfoInsert);
        return toAjax(spaceMemberInfoService.insertSpaceMemberInfo(spaceMemberInfo));
    }

    /**
     * 修改空间成员信息
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceMemberInfo:edit')")
    @Log(title = "空间成员信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SpaceMemberInfoEdit spaceMemberInfoEdit)
    {
        SpaceMemberInfo spaceMemberInfo = SpaceMemberInfoEdit.editToObj(spaceMemberInfoEdit);
        return toAjax(spaceMemberInfoService.updateSpaceMemberInfo(spaceMemberInfo));
    }

    /**
     * 删除空间成员信息
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceMemberInfo:remove')")
    @Log(title = "空间成员信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{memberIds}")
    public AjaxResult remove(@PathVariable String[] memberIds)
    {
        return toAjax(spaceMemberInfoService.deleteSpaceMemberInfoByMemberIds(memberIds));
    }
}
