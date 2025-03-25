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
import com.lz.picture.model.domain.PictureTagRelInfo;
import com.lz.picture.model.vo.pictureTagRelInfo.PictureTagRelInfoVo;
import com.lz.picture.model.dto.pictureTagRelInfo.PictureTagRelInfoQuery;
import com.lz.picture.model.dto.pictureTagRelInfo.PictureTagRelInfoInsert;
import com.lz.picture.model.dto.pictureTagRelInfo.PictureTagRelInfoEdit;
import com.lz.picture.service.IPictureTagRelInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 图片标签关联Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/pictureTagRelInfo")
public class PictureTagRelInfoController extends BaseController
{
    @Resource
    private IPictureTagRelInfoService pictureTagRelInfoService;

    /**
     * 查询图片标签关联列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureTagRelInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PictureTagRelInfoQuery pictureTagRelInfoQuery)
    {
        PictureTagRelInfo pictureTagRelInfo = PictureTagRelInfoQuery.queryToObj(pictureTagRelInfoQuery);
        startPage();
        List<PictureTagRelInfo> list = pictureTagRelInfoService.selectPictureTagRelInfoList(pictureTagRelInfo);
        List<PictureTagRelInfoVo> listVo= list.stream().map(PictureTagRelInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出图片标签关联列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureTagRelInfo:export')")
    @Log(title = "图片标签关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureTagRelInfoQuery pictureTagRelInfoQuery)
    {
        PictureTagRelInfo pictureTagRelInfo = PictureTagRelInfoQuery.queryToObj(pictureTagRelInfoQuery);
        List<PictureTagRelInfo> list = pictureTagRelInfoService.selectPictureTagRelInfoList(pictureTagRelInfo);
        ExcelUtil<PictureTagRelInfo> util = new ExcelUtil<PictureTagRelInfo>(PictureTagRelInfo.class);
        util.exportExcel(response, list, "图片标签关联数据");
    }

    /**
     * 获取图片标签关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureTagRelInfo:query')")
    @GetMapping(value = "/{pictureId}")
    public AjaxResult getInfo(@PathVariable("pictureId") String pictureId)
    {
        PictureTagRelInfo pictureTagRelInfo = pictureTagRelInfoService.selectPictureTagRelInfoByPictureId(pictureId);
        return success(PictureTagRelInfoVo.objToVo(pictureTagRelInfo));
    }

    /**
     * 新增图片标签关联
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureTagRelInfo:add')")
    @Log(title = "图片标签关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PictureTagRelInfoInsert pictureTagRelInfoInsert)
    {
        PictureTagRelInfo pictureTagRelInfo = PictureTagRelInfoInsert.insertToObj(pictureTagRelInfoInsert);
        return toAjax(pictureTagRelInfoService.insertPictureTagRelInfo(pictureTagRelInfo));
    }

    /**
     * 修改图片标签关联
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureTagRelInfo:edit')")
    @Log(title = "图片标签关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PictureTagRelInfoEdit pictureTagRelInfoEdit)
    {
        PictureTagRelInfo pictureTagRelInfo = PictureTagRelInfoEdit.editToObj(pictureTagRelInfoEdit);
        return toAjax(pictureTagRelInfoService.updatePictureTagRelInfo(pictureTagRelInfo));
    }

    /**
     * 删除图片标签关联
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureTagRelInfo:remove')")
    @Log(title = "图片标签关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{pictureIds}")
    public AjaxResult remove(@PathVariable String[] pictureIds)
    {
        return toAjax(pictureTagRelInfoService.deletePictureTagRelInfoByPictureIds(pictureIds));
    }
}
