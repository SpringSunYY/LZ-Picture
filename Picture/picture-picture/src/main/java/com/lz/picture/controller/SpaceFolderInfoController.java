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
import com.lz.picture.model.domain.SpaceFolderInfo;
import com.lz.picture.model.vo.spaceFolderInfo.SpaceFolderInfoVo;
import com.lz.picture.model.dto.spaceFolderInfo.SpaceFolderInfoQuery;
import com.lz.picture.model.dto.spaceFolderInfo.SpaceFolderInfoInsert;
import com.lz.picture.model.dto.spaceFolderInfo.SpaceFolderInfoEdit;
import com.lz.picture.service.ISpaceFolderInfoService;
import com.lz.common.utils.poi.ExcelUtil;

/**
 * 空间文件夹Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/spaceFolderInfo")
public class SpaceFolderInfoController extends BaseController
{
    @Resource
    private ISpaceFolderInfoService spaceFolderInfoService;

    /**
     * 查询空间文件夹列表
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceFolderInfo:list')")
    @GetMapping("/list")
        public AjaxResult list(SpaceFolderInfoQuery spaceFolderInfoQuery)
        {
            SpaceFolderInfo spaceFolderInfo = SpaceFolderInfoQuery.queryToObj(spaceFolderInfoQuery);
            List<SpaceFolderInfo> list = spaceFolderInfoService.selectSpaceFolderInfoList(spaceFolderInfo);
            List<SpaceFolderInfoVo> listVo= list.stream().map(SpaceFolderInfoVo::objToVo).collect(Collectors.toList());
            return success(listVo);
        }

    /**
     * 导出空间文件夹列表
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceFolderInfo:export')")
    @Log(title = "空间文件夹", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SpaceFolderInfoQuery spaceFolderInfoQuery)
    {
        SpaceFolderInfo spaceFolderInfo = SpaceFolderInfoQuery.queryToObj(spaceFolderInfoQuery);
        List<SpaceFolderInfo> list = spaceFolderInfoService.selectSpaceFolderInfoList(spaceFolderInfo);
        ExcelUtil<SpaceFolderInfo> util = new ExcelUtil<SpaceFolderInfo>(SpaceFolderInfo.class);
        util.exportExcel(response, list, "空间文件夹数据");
    }

    /**
     * 获取空间文件夹详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceFolderInfo:query')")
    @GetMapping(value = "/{folderId}")
    public AjaxResult getInfo(@PathVariable("folderId") String folderId)
    {
        SpaceFolderInfo spaceFolderInfo = spaceFolderInfoService.selectSpaceFolderInfoByFolderId(folderId);
        return success(SpaceFolderInfoVo.objToVo(spaceFolderInfo));
    }

    /**
     * 新增空间文件夹
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceFolderInfo:add')")
    @Log(title = "空间文件夹", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SpaceFolderInfoInsert spaceFolderInfoInsert)
    {
        SpaceFolderInfo spaceFolderInfo = SpaceFolderInfoInsert.insertToObj(spaceFolderInfoInsert);
        return toAjax(spaceFolderInfoService.insertSpaceFolderInfo(spaceFolderInfo));
    }

    /**
     * 修改空间文件夹
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceFolderInfo:edit')")
    @Log(title = "空间文件夹", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SpaceFolderInfoEdit spaceFolderInfoEdit)
    {
        SpaceFolderInfo spaceFolderInfo = SpaceFolderInfoEdit.editToObj(spaceFolderInfoEdit);
        return toAjax(spaceFolderInfoService.updateSpaceFolderInfo(spaceFolderInfo));
    }

    /**
     * 删除空间文件夹
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceFolderInfo:remove')")
    @Log(title = "空间文件夹", businessType = BusinessType.DELETE)
    @DeleteMapping("/{folderIds}")
    public AjaxResult remove(@PathVariable String[] folderIds)
    {
        return toAjax(spaceFolderInfoService.deleteSpaceFolderInfoByFolderIds(folderIds));
    }
}
