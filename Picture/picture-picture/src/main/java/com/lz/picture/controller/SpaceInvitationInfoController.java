package com.lz.picture.controller;

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
import com.lz.picture.model.domain.SpaceInvitationInfo;
import com.lz.picture.model.vo.spaceInvitationInfo.SpaceInvitationInfoVo;
import com.lz.picture.model.dto.spaceInvitationInfo.SpaceInvitationInfoQuery;
import com.lz.picture.model.dto.spaceInvitationInfo.SpaceInvitationInfoInsert;
import com.lz.picture.model.dto.spaceInvitationInfo.SpaceInvitationInfoEdit;
import com.lz.picture.service.ISpaceInvitationInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 空间成员邀请记录Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/spaceInvitationInfo")
public class SpaceInvitationInfoController extends BaseController
{
    @Resource
    private ISpaceInvitationInfoService spaceInvitationInfoService;

    /**
     * 查询空间成员邀请记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceInvitationInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpaceInvitationInfoQuery spaceInvitationInfoQuery)
    {
        SpaceInvitationInfo spaceInvitationInfo = SpaceInvitationInfoQuery.queryToObj(spaceInvitationInfoQuery);
        startPage();
        List<SpaceInvitationInfo> list = spaceInvitationInfoService.selectSpaceInvitationInfoList(spaceInvitationInfo);
        List<SpaceInvitationInfoVo> listVo= list.stream().map(SpaceInvitationInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出空间成员邀请记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceInvitationInfo:export')")
    @Log(title = "空间成员邀请记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SpaceInvitationInfoQuery spaceInvitationInfoQuery)
    {
        SpaceInvitationInfo spaceInvitationInfo = SpaceInvitationInfoQuery.queryToObj(spaceInvitationInfoQuery);
        List<SpaceInvitationInfo> list = spaceInvitationInfoService.selectSpaceInvitationInfoList(spaceInvitationInfo);
        ExcelUtil<SpaceInvitationInfo> util = new ExcelUtil<SpaceInvitationInfo>(SpaceInvitationInfo.class);
        util.exportExcel(response, list, "空间成员邀请记录数据");
    }

    /**
     * 获取空间成员邀请记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceInvitationInfo:query')")
    @GetMapping(value = "/{invitationId}")
    public AjaxResult getInfo(@PathVariable("invitationId") String invitationId)
    {
        SpaceInvitationInfo spaceInvitationInfo = spaceInvitationInfoService.selectSpaceInvitationInfoByInvitationId(invitationId);
        return success(SpaceInvitationInfoVo.objToVo(spaceInvitationInfo));
    }

    /**
     * 新增空间成员邀请记录
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceInvitationInfo:add')")
    @Log(title = "空间成员邀请记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SpaceInvitationInfoInsert spaceInvitationInfoInsert)
    {
        SpaceInvitationInfo spaceInvitationInfo = SpaceInvitationInfoInsert.insertToObj(spaceInvitationInfoInsert);
        return toAjax(spaceInvitationInfoService.insertSpaceInvitationInfo(spaceInvitationInfo));
    }

    /**
     * 修改空间成员邀请记录
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceInvitationInfo:edit')")
    @Log(title = "空间成员邀请记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SpaceInvitationInfoEdit spaceInvitationInfoEdit)
    {
        SpaceInvitationInfo spaceInvitationInfo = SpaceInvitationInfoEdit.editToObj(spaceInvitationInfoEdit);
        return toAjax(spaceInvitationInfoService.updateSpaceInvitationInfo(spaceInvitationInfo));
    }

    /**
     * 删除空间成员邀请记录
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceInvitationInfo:remove')")
    @Log(title = "空间成员邀请记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{invitationIds}")
    public AjaxResult remove(@PathVariable String[] invitationIds)
    {
        return toAjax(spaceInvitationInfoService.deleteSpaceInvitationInfoByInvitationIds(invitationIds));
    }
}
