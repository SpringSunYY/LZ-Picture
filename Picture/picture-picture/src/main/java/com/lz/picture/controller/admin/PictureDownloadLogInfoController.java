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
import com.lz.picture.model.domain.PictureDownloadLogInfo;
import com.lz.picture.model.vo.pictureDownloadLogInfo.PictureDownloadLogInfoVo;
import com.lz.picture.model.dto.pictureDownloadLogInfo.PictureDownloadLogInfoQuery;
import com.lz.picture.model.dto.pictureDownloadLogInfo.PictureDownloadLogInfoInsert;
import com.lz.picture.model.dto.pictureDownloadLogInfo.PictureDownloadLogInfoEdit;
import com.lz.picture.service.IPictureDownloadLogInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 图片下载记录Controller
 *
 * @author YY
 * @date 2025-05-24
 */
@RestController
@RequestMapping("/admin/picture/pictureDownloadLogInfo")
public class PictureDownloadLogInfoController extends BaseController
{
    @Resource
    private IPictureDownloadLogInfoService pictureDownloadLogInfoService;

    /**
     * 查询图片下载记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureDownloadLogInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PictureDownloadLogInfoQuery pictureDownloadLogInfoQuery)
    {
        PictureDownloadLogInfo pictureDownloadLogInfo = PictureDownloadLogInfoQuery.queryToObj(pictureDownloadLogInfoQuery);
        startPage();
        List<PictureDownloadLogInfo> list = pictureDownloadLogInfoService.selectPictureDownloadLogInfoList(pictureDownloadLogInfo);
        List<PictureDownloadLogInfoVo> listVo= list.stream().map(PictureDownloadLogInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出图片下载记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureDownloadLogInfo:export')")
    @Log(title = "图片下载记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureDownloadLogInfoQuery pictureDownloadLogInfoQuery)
    {
        PictureDownloadLogInfo pictureDownloadLogInfo = PictureDownloadLogInfoQuery.queryToObj(pictureDownloadLogInfoQuery);
        List<PictureDownloadLogInfo> list = pictureDownloadLogInfoService.selectPictureDownloadLogInfoList(pictureDownloadLogInfo);
        ExcelUtil<PictureDownloadLogInfo> util = new ExcelUtil<PictureDownloadLogInfo>(PictureDownloadLogInfo.class);
        util.exportExcel(response, list, "图片下载记录数据");
    }

    /**
     * 获取图片下载记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureDownloadLogInfo:query')")
    @GetMapping(value = "/{downloadId}")
    public AjaxResult getInfo(@PathVariable("downloadId") String downloadId)
    {
        PictureDownloadLogInfo pictureDownloadLogInfo = pictureDownloadLogInfoService.selectPictureDownloadLogInfoByDownloadId(downloadId);
        return success(PictureDownloadLogInfoVo.objToVo(pictureDownloadLogInfo));
    }

    /**
     * 新增图片下载记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureDownloadLogInfo:add')")
    @Log(title = "图片下载记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PictureDownloadLogInfoInsert pictureDownloadLogInfoInsert)
    {
        PictureDownloadLogInfo pictureDownloadLogInfo = PictureDownloadLogInfoInsert.insertToObj(pictureDownloadLogInfoInsert);
        return toAjax(pictureDownloadLogInfoService.insertPictureDownloadLogInfo(pictureDownloadLogInfo));
    }

    /**
     * 修改图片下载记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureDownloadLogInfo:edit')")
    @Log(title = "图片下载记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PictureDownloadLogInfoEdit pictureDownloadLogInfoEdit)
    {
        PictureDownloadLogInfo pictureDownloadLogInfo = PictureDownloadLogInfoEdit.editToObj(pictureDownloadLogInfoEdit);
        return toAjax(pictureDownloadLogInfoService.updatePictureDownloadLogInfo(pictureDownloadLogInfo));
    }

    /**
     * 删除图片下载记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureDownloadLogInfo:remove')")
    @Log(title = "图片下载记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{downloadIds}")
    public AjaxResult remove(@PathVariable String[] downloadIds)
    {
        return toAjax(pictureDownloadLogInfoService.deletePictureDownloadLogInfoByDownloadIds(downloadIds));
    }
}
