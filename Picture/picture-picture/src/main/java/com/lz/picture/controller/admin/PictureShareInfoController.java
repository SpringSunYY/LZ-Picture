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
import com.lz.picture.model.domain.PictureShareInfo;
import com.lz.picture.model.vo.pictureShareInfo.PictureShareInfoVo;
import com.lz.picture.model.dto.pictureShareInfo.PictureShareInfoQuery;
import com.lz.picture.model.dto.pictureShareInfo.PictureShareInfoInsert;
import com.lz.picture.model.dto.pictureShareInfo.PictureShareInfoEdit;
import com.lz.picture.service.IPictureShareInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 图片转发记录Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/pictureShareInfo")
public class PictureShareInfoController extends BaseController
{
    @Resource
    private IPictureShareInfoService pictureShareInfoService;

    /**
     * 查询图片转发记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureShareInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PictureShareInfoQuery pictureShareInfoQuery)
    {
        PictureShareInfo pictureShareInfo = PictureShareInfoQuery.queryToObj(pictureShareInfoQuery);
        startPage();
        List<PictureShareInfo> list = pictureShareInfoService.selectPictureShareInfoList(pictureShareInfo);
        List<PictureShareInfoVo> listVo= list.stream().map(PictureShareInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出图片转发记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureShareInfo:export')")
    @Log(title = "图片转发记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureShareInfoQuery pictureShareInfoQuery)
    {
        PictureShareInfo pictureShareInfo = PictureShareInfoQuery.queryToObj(pictureShareInfoQuery);
        List<PictureShareInfo> list = pictureShareInfoService.selectPictureShareInfoList(pictureShareInfo);
        ExcelUtil<PictureShareInfo> util = new ExcelUtil<PictureShareInfo>(PictureShareInfo.class);
        util.exportExcel(response, list, "图片转发记录数据");
    }

    /**
     * 获取图片转发记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureShareInfo:query')")
    @GetMapping(value = "/{shareId}")
    public AjaxResult getInfo(@PathVariable("shareId") String shareId)
    {
        PictureShareInfo pictureShareInfo = pictureShareInfoService.selectPictureShareInfoByShareId(shareId);
        return success(PictureShareInfoVo.objToVo(pictureShareInfo));
    }

    /**
     * 新增图片转发记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureShareInfo:add')")
    @Log(title = "图片转发记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PictureShareInfoInsert pictureShareInfoInsert)
    {
        PictureShareInfo pictureShareInfo = PictureShareInfoInsert.insertToObj(pictureShareInfoInsert);
        return toAjax(pictureShareInfoService.insertPictureShareInfo(pictureShareInfo));
    }

    /**
     * 修改图片转发记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureShareInfo:edit')")
    @Log(title = "图片转发记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PictureShareInfoEdit pictureShareInfoEdit)
    {
        PictureShareInfo pictureShareInfo = PictureShareInfoEdit.editToObj(pictureShareInfoEdit);
        return toAjax(pictureShareInfoService.updatePictureShareInfo(pictureShareInfo));
    }

    /**
     * 删除图片转发记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureShareInfo:remove')")
    @Log(title = "图片转发记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{shareIds}")
    public AjaxResult remove(@PathVariable String[] shareIds)
    {
        return toAjax(pictureShareInfoService.deletePictureShareInfoByShareIds(shareIds));
    }
}
