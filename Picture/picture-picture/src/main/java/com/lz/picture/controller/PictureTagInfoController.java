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
import com.lz.picture.model.domain.PictureTagInfo;
import com.lz.picture.model.vo.pictureTagInfo.PictureTagInfoVo;
import com.lz.picture.model.dto.pictureTagInfo.PictureTagInfoQuery;
import com.lz.picture.model.dto.pictureTagInfo.PictureTagInfoInsert;
import com.lz.picture.model.dto.pictureTagInfo.PictureTagInfoEdit;
import com.lz.picture.service.IPictureTagInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 图片标签信息Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/pictureTagInfo")
public class PictureTagInfoController extends BaseController
{
    @Resource
    private IPictureTagInfoService pictureTagInfoService;

    /**
     * 查询图片标签信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureTagInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PictureTagInfoQuery pictureTagInfoQuery)
    {
        PictureTagInfo pictureTagInfo = PictureTagInfoQuery.queryToObj(pictureTagInfoQuery);
        startPage();
        List<PictureTagInfo> list = pictureTagInfoService.selectPictureTagInfoList(pictureTagInfo);
        List<PictureTagInfoVo> listVo= list.stream().map(PictureTagInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出图片标签信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureTagInfo:export')")
    @Log(title = "图片标签信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureTagInfoQuery pictureTagInfoQuery)
    {
        PictureTagInfo pictureTagInfo = PictureTagInfoQuery.queryToObj(pictureTagInfoQuery);
        List<PictureTagInfo> list = pictureTagInfoService.selectPictureTagInfoList(pictureTagInfo);
        ExcelUtil<PictureTagInfo> util = new ExcelUtil<PictureTagInfo>(PictureTagInfo.class);
        util.exportExcel(response, list, "图片标签信息数据");
    }

    /**
     * 获取图片标签信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureTagInfo:query')")
    @GetMapping(value = "/{tagId}")
    public AjaxResult getInfo(@PathVariable("tagId") String tagId)
    {
        PictureTagInfo pictureTagInfo = pictureTagInfoService.selectPictureTagInfoByTagId(tagId);
        return success(PictureTagInfoVo.objToVo(pictureTagInfo));
    }

    /**
     * 新增图片标签信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureTagInfo:add')")
    @Log(title = "图片标签信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PictureTagInfoInsert pictureTagInfoInsert)
    {
        PictureTagInfo pictureTagInfo = PictureTagInfoInsert.insertToObj(pictureTagInfoInsert);
        return toAjax(pictureTagInfoService.insertPictureTagInfo(pictureTagInfo));
    }

    /**
     * 修改图片标签信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureTagInfo:edit')")
    @Log(title = "图片标签信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PictureTagInfoEdit pictureTagInfoEdit)
    {
        PictureTagInfo pictureTagInfo = PictureTagInfoEdit.editToObj(pictureTagInfoEdit);
        return toAjax(pictureTagInfoService.updatePictureTagInfo(pictureTagInfo));
    }

    /**
     * 删除图片标签信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureTagInfo:remove')")
    @Log(title = "图片标签信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tagIds}")
    public AjaxResult remove(@PathVariable String[] tagIds)
    {
        return toAjax(pictureTagInfoService.deletePictureTagInfoByTagIds(tagIds));
    }
}
