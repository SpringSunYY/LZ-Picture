package com.lz.picture.controller.admin;

import com.lz.common.annotation.Log;
import com.lz.common.config.OssConfig;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.picture.model.domain.PictureDownloadLogInfo;
import com.lz.picture.model.dto.pictureDownloadLogInfo.PictureDownloadLogInfoEdit;
import com.lz.picture.model.dto.pictureDownloadLogInfo.PictureDownloadLogInfoInsert;
import com.lz.picture.model.dto.pictureDownloadLogInfo.PictureDownloadLogInfoQuery;
import com.lz.picture.model.vo.pictureDownloadLogInfo.PictureDownloadLogInfoVo;
import com.lz.picture.service.IPictureDownloadLogInfoService;
import com.lz.system.service.ISysConfigService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.lz.common.constant.ConfigConstants.PICTURE_P;
import static com.lz.config.utils.ConfigInfoUtils.PICTURE_COVER_P_VALUE;

/**
 * 图片下载记录Controller
 *
 * @author YY
 * @date 2025-05-24
 */
@RestController
@RequestMapping("/admin/picture/pictureDownloadLogInfo")
public class PictureDownloadLogInfoController extends BaseController {
    @Resource
    private IPictureDownloadLogInfoService pictureDownloadLogInfoService;

    @Resource
    private ISysConfigService sysConfigService;

    /**
     * 查询图片下载记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureDownloadLogInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PictureDownloadLogInfoQuery pictureDownloadLogInfoQuery) {
        PictureDownloadLogInfo pictureDownloadLogInfo = PictureDownloadLogInfoQuery.queryToObj(pictureDownloadLogInfoQuery);
        startPage();
        List<PictureDownloadLogInfo> list = pictureDownloadLogInfoService.selectPictureDownloadLogInfoList(pictureDownloadLogInfo);
        List<PictureDownloadLogInfoVo> listVo = new ArrayList<>();
        String inCache = sysConfigService.selectConfigByKey(PICTURE_P);
        for (PictureDownloadLogInfo info : list) {
            PictureDownloadLogInfoVo obj = PictureDownloadLogInfoVo.objToVo(info);
            obj.setThumbnailUrl(OssConfig.builderPictureUrl(info.getThumbnailUrl(), inCache));
            listVo.add(obj);
        }
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
    public void export(HttpServletResponse response, PictureDownloadLogInfoQuery pictureDownloadLogInfoQuery) {
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
    public AjaxResult getInfo(@PathVariable("downloadId") String downloadId) {
        PictureDownloadLogInfo pictureDownloadLogInfo = pictureDownloadLogInfoService.selectPictureDownloadLogInfoByDownloadId(downloadId);
        return success(PictureDownloadLogInfoVo.objToVo(pictureDownloadLogInfo));
    }

    /**
     * 新增图片下载记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureDownloadLogInfo:add')")
    @Log(title = "图片下载记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PictureDownloadLogInfoInsert pictureDownloadLogInfoInsert) {
        PictureDownloadLogInfo pictureDownloadLogInfo = PictureDownloadLogInfoInsert.insertToObj(pictureDownloadLogInfoInsert);
        return toAjax(pictureDownloadLogInfoService.insertPictureDownloadLogInfo(pictureDownloadLogInfo));
    }

    /**
     * 修改图片下载记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureDownloadLogInfo:edit')")
    @Log(title = "图片下载记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PictureDownloadLogInfoEdit pictureDownloadLogInfoEdit) {
        PictureDownloadLogInfo pictureDownloadLogInfo = PictureDownloadLogInfoEdit.editToObj(pictureDownloadLogInfoEdit);
        return toAjax(pictureDownloadLogInfoService.updatePictureDownloadLogInfo(pictureDownloadLogInfo));
    }

    /**
     * 删除图片下载记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureDownloadLogInfo:remove')")
    @Log(title = "图片下载记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{downloadIds}")
    public AjaxResult remove(@PathVariable String[] downloadIds) {
        return toAjax(pictureDownloadLogInfoService.deletePictureDownloadLogInfoByDownloadIds(downloadIds));
    }
}
