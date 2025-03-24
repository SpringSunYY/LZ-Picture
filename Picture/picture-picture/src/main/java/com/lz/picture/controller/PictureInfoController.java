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
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.vo.pictureInfo.PictureInfoVo;
import com.lz.picture.model.dto.pictureInfo.PictureInfoQuery;
import com.lz.picture.model.dto.pictureInfo.PictureInfoInsert;
import com.lz.picture.model.dto.pictureInfo.PictureInfoEdit;
import com.lz.picture.service.IPictureInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 图片信息Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/pictureInfo")
public class PictureInfoController extends BaseController
{
    @Resource
    private IPictureInfoService pictureInfoService;

    /**
     * 查询图片信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PictureInfoQuery pictureInfoQuery)
    {
        PictureInfo pictureInfo = PictureInfoQuery.queryToObj(pictureInfoQuery);
        startPage();
        List<PictureInfo> list = pictureInfoService.selectPictureInfoList(pictureInfo);
        List<PictureInfoVo> listVo= list.stream().map(PictureInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出图片信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureInfo:export')")
    @Log(title = "图片信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureInfoQuery pictureInfoQuery)
    {
        PictureInfo pictureInfo = PictureInfoQuery.queryToObj(pictureInfoQuery);
        List<PictureInfo> list = pictureInfoService.selectPictureInfoList(pictureInfo);
        ExcelUtil<PictureInfo> util = new ExcelUtil<PictureInfo>(PictureInfo.class);
        util.exportExcel(response, list, "图片信息数据");
    }

    /**
     * 获取图片信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureInfo:query')")
    @GetMapping(value = "/{pictureId}")
    public AjaxResult getInfo(@PathVariable("pictureId") String pictureId)
    {
        PictureInfo pictureInfo = pictureInfoService.selectPictureInfoByPictureId(pictureId);
        return success(PictureInfoVo.objToVo(pictureInfo));
    }

    /**
     * 新增图片信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureInfo:add')")
    @Log(title = "图片信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PictureInfoInsert pictureInfoInsert)
    {
        PictureInfo pictureInfo = PictureInfoInsert.insertToObj(pictureInfoInsert);
        return toAjax(pictureInfoService.insertPictureInfo(pictureInfo));
    }

    /**
     * 修改图片信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureInfo:edit')")
    @Log(title = "图片信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PictureInfoEdit pictureInfoEdit)
    {
        PictureInfo pictureInfo = PictureInfoEdit.editToObj(pictureInfoEdit);
        return toAjax(pictureInfoService.updatePictureInfo(pictureInfo));
    }

    /**
     * 删除图片信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureInfo:remove')")
    @Log(title = "图片信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{pictureIds}")
    public AjaxResult remove(@PathVariable String[] pictureIds)
    {
        return toAjax(pictureInfoService.deletePictureInfoByPictureIds(pictureIds));
    }
}
