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
import com.lz.picture.model.domain.PictureDownloadLog;
import com.lz.picture.model.vo.pictureDownloadLog.PictureDownloadLogVo;
import com.lz.picture.model.dto.pictureDownloadLog.PictureDownloadLogQuery;
import com.lz.picture.model.dto.pictureDownloadLog.PictureDownloadLogInsert;
import com.lz.picture.model.dto.pictureDownloadLog.PictureDownloadLogEdit;
import com.lz.picture.service.IPictureDownloadLogService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 图片下载记录Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/pictureDownloadLog")
public class PictureDownloadLogController extends BaseController
{
    @Resource
    private IPictureDownloadLogService pictureDownloadLogService;

    /**
     * 查询图片下载记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureDownloadLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(PictureDownloadLogQuery pictureDownloadLogQuery)
    {
        PictureDownloadLog pictureDownloadLog = PictureDownloadLogQuery.queryToObj(pictureDownloadLogQuery);
        startPage();
        List<PictureDownloadLog> list = pictureDownloadLogService.selectPictureDownloadLogList(pictureDownloadLog);
        List<PictureDownloadLogVo> listVo= list.stream().map(PictureDownloadLogVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出图片下载记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureDownloadLog:export')")
    @Log(title = "图片下载记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureDownloadLogQuery pictureDownloadLogQuery)
    {
        PictureDownloadLog pictureDownloadLog = PictureDownloadLogQuery.queryToObj(pictureDownloadLogQuery);
        List<PictureDownloadLog> list = pictureDownloadLogService.selectPictureDownloadLogList(pictureDownloadLog);
        ExcelUtil<PictureDownloadLog> util = new ExcelUtil<PictureDownloadLog>(PictureDownloadLog.class);
        util.exportExcel(response, list, "图片下载记录数据");
    }

    /**
     * 获取图片下载记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureDownloadLog:query')")
    @GetMapping(value = "/{downloadId}")
    public AjaxResult getInfo(@PathVariable("downloadId") String downloadId)
    {
        PictureDownloadLog pictureDownloadLog = pictureDownloadLogService.selectPictureDownloadLogByDownloadId(downloadId);
        return success(PictureDownloadLogVo.objToVo(pictureDownloadLog));
    }

    /**
     * 新增图片下载记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureDownloadLog:add')")
    @Log(title = "图片下载记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PictureDownloadLogInsert pictureDownloadLogInsert)
    {
        PictureDownloadLog pictureDownloadLog = PictureDownloadLogInsert.insertToObj(pictureDownloadLogInsert);
        return toAjax(pictureDownloadLogService.insertPictureDownloadLog(pictureDownloadLog));
    }

    /**
     * 修改图片下载记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureDownloadLog:edit')")
    @Log(title = "图片下载记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PictureDownloadLogEdit pictureDownloadLogEdit)
    {
        PictureDownloadLog pictureDownloadLog = PictureDownloadLogEdit.editToObj(pictureDownloadLogEdit);
        return toAjax(pictureDownloadLogService.updatePictureDownloadLog(pictureDownloadLog));
    }

    /**
     * 删除图片下载记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureDownloadLog:remove')")
    @Log(title = "图片下载记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{downloadIds}")
    public AjaxResult remove(@PathVariable String[] downloadIds)
    {
        return toAjax(pictureDownloadLogService.deletePictureDownloadLogByDownloadIds(downloadIds));
    }
}
