package com.lz.picture.controller.admin;

import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.file.FileUtils;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.picture.model.domain.StatisticsInfo;
import com.lz.picture.model.dto.statistics.KeywordStatisticsRequest;
import com.lz.picture.model.dto.statistics.BasePictureStatisticsRequest;
import com.lz.picture.model.dto.statistics.PictureStatisticsRequest;
import com.lz.picture.model.dto.statisticsInfo.*;
import com.lz.picture.model.vo.statisticsInfo.StatisticsInfoVo;
import com.lz.picture.service.IStatisticsInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 统计信息Controller
 *
 * @author YY
 * @date 2025-07-17
 */
@RestController
@RequestMapping("/admin/picture/statisticsInfo")
@Slf4j
public class StatisticsInfoController extends BaseController {
    @Resource
    private IStatisticsInfoService statisticsInfoService;

    /**
     * 查询统计信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:statisticsInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(StatisticsInfoQuery statisticsInfoQuery) {
        StatisticsInfo statisticsInfo = StatisticsInfoQuery.queryToObj(statisticsInfoQuery);
        startPage();
        List<StatisticsInfo> list = statisticsInfoService.selectStatisticsInfoList(statisticsInfo);
        List<StatisticsInfoVo> listVo = list.stream().map(StatisticsInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出统计信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:statisticsInfo:export')")
    @Log(title = "统计信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StatisticsInfoQuery statisticsInfoQuery) {
        StatisticsInfo statisticsInfo = StatisticsInfoQuery.queryToObj(statisticsInfoQuery);
        List<StatisticsInfo> list = statisticsInfoService.selectStatisticsInfoList(statisticsInfo);
        ExcelUtil<StatisticsInfo> util = new ExcelUtil<StatisticsInfo>(StatisticsInfo.class);
        util.exportExcel(response, list, "统计信息数据");
    }

    /**
     * 获取统计信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:statisticsInfo:query')")
    @GetMapping(value = "/{statisticsId}")
    public AjaxResult getInfo(@PathVariable("statisticsId") String statisticsId) {
        StatisticsInfo statisticsInfo = statisticsInfoService.selectStatisticsInfoByStatisticsId(statisticsId);
        return success(StatisticsInfoVo.objToVo(statisticsInfo));
    }

    /**
     * 新增统计信息
     */
    @PreAuthorize("@ss.hasPermi('picture:statisticsInfo:add')")
    @Log(title = "统计信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StatisticsInfoInsert statisticsInfoInsert) {
        StatisticsInfo statisticsInfo = StatisticsInfoInsert.insertToObj(statisticsInfoInsert);
        return toAjax(statisticsInfoService.insertStatisticsInfo(statisticsInfo));
    }

    /**
     * 修改统计信息
     */
    @PreAuthorize("@ss.hasPermi('picture:statisticsInfo:edit')")
    @Log(title = "统计信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StatisticsInfoEdit statisticsInfoEdit) {
        StatisticsInfo statisticsInfo = StatisticsInfoEdit.editToObj(statisticsInfoEdit);
        return toAjax(statisticsInfoService.updateStatisticsInfo(statisticsInfo));
    }

    /**
     * 删除统计信息
     */
    @PreAuthorize("@ss.hasPermi('picture:statisticsInfo:remove')")
    @Log(title = "统计信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{statisticsIds}")
    public AjaxResult remove(@PathVariable String[] statisticsIds) {
        return toAjax(statisticsInfoService.deleteStatisticsInfoByStatisticsIds(statisticsIds));
    }

    /**
     * 获取期数信息
     */
    @GetMapping("/stages")
    public AjaxResult stages(StatisticsInfoRequest request) {
        return success(statisticsInfoService.getStatisticsInfoStages(request));
    }

    @PreAuthorize("@ss.hasPermi('picture:download:hot')")
    @GetMapping("/download/hot")
    public void hot(Boolean isDelete, String type, String commonKey, String statisticsKey, Long stage, int number, HttpServletResponse response, HttpServletRequest request) {
        try {
            StatisticsFileDto dto = statisticsInfoService.getStatisticsPictureHotFilePath(type, commonKey, statisticsKey, stage, number);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, dto.getFileName());
            FileUtils.writeBytes(dto.getFilePath(), response.getOutputStream());
            if (isDelete) {
                FileUtils.deleteFile(dto.getFilePath());
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 搜索关键词统计
     */
    @PreAuthorize("@ss.hasPermi('picture:statistics')")
    @GetMapping("/search/keyword")
    public AjaxResult searchKeywordStatistics(@Validated KeywordStatisticsRequest request) {
        return success(statisticsInfoService.searchKeywordStatistics(request));
    }

    /**
     * 图片标签关键词统计
     */
    @PreAuthorize("@ss.hasPermi('picture:statistics')")
    @GetMapping("/tag/keyword")
    public AjaxResult tagKeywordStatistics(@Validated KeywordStatisticsRequest request) {
        return success(statisticsInfoService.tagKeywordStatistics(request));
    }

    /**
     * 用户行文统计
     */
    @PreAuthorize("@ss.hasPermi('picture:statistics')")
    @GetMapping("/user/behavior")
    public AjaxResult userBehaviorStatistics(@Validated BasePictureStatisticsRequest request) {
        return success(statisticsInfoService.userBehaviorStatistics(request));
    }

    /**
     * 图片下载
     */
    @PreAuthorize("@ss.hasPermi('picture:statistics')")
    @GetMapping("/picture/download")
    public AjaxResult pictureDownloadStatistics(@Validated BasePictureStatisticsRequest request) {
        return success(statisticsInfoService.pictureDownloadStatistics(request));
    }

    /**
     * 空间统计
     */
    @PreAuthorize("@ss.hasPermi('picture:statistics')")
    @GetMapping("/space")
    public AjaxResult spaceStatistics(@Validated BasePictureStatisticsRequest request) {
        return success(statisticsInfoService.spaceStatistics(request));
    }

    /**
     * 图片统计
     */
    @PreAuthorize("@ss.hasPermi('picture:statistics')")
    @GetMapping("/picture")
    public AjaxResult pictureStatistics(@Validated PictureStatisticsRequest request) {
        return success(statisticsInfoService.pictureStatistics(request));
    }

    /**
     * 图片状态统计
     */
    @PreAuthorize("@ss.hasPermi('picture:statistics')")
    @GetMapping("/picture/status")
    public AjaxResult pictureStatusStatistics() {
        return success(statisticsInfoService.pictureStatusStatistics());
    }

    /**
     * 图片上传类型
     */
    @PreAuthorize("@ss.hasPermi('picture:statistics')")
    @GetMapping("/picture/upload/type")
    public AjaxResult pictureUploadTypeStatistics() {
        return success(statisticsInfoService.pictureUploadTypeStatistics());
    }

    /**
     * 空间文件总数
     */
    @PreAuthorize("@ss.hasPermi('picture:statistics')")
    @GetMapping("/space/file/total")
    public AjaxResult spaceFileTotalStatistics() {
        return success(statisticsInfoService.spaceFileTotalStatistics());
    }

    /**
     * 空间文件大小
     */
    @PreAuthorize("@ss.hasPermi('picture:statistics')")
    @GetMapping("/space/file/size")
    public AjaxResult spaceFileSizeStatistics() {
        return success(statisticsInfoService.spaceFileSizeStatistics());
    }
}
