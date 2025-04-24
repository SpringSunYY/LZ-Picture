package com.lz.config.controller.admin;

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
import com.lz.config.model.domain.FileLogInfo;
import com.lz.config.model.vo.fileLogInfo.FileLogInfoVo;
import com.lz.config.model.dto.fileLogInfo.FileLogInfoQuery;
import com.lz.config.model.dto.fileLogInfo.FileLogInfoInsert;
import com.lz.config.model.dto.fileLogInfo.FileLogInfoEdit;
import com.lz.config.service.IFileLogInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 文件日志Controller
 *
 * @author YY
 * @date 2025-04-24
 */
@RestController
@RequestMapping("/admin/config/fileLogInfo")
public class FileLogInfoController extends BaseController
{
    @Resource
    private IFileLogInfoService fileLogInfoService;

    /**
     * 查询文件日志列表
     */
    @PreAuthorize("@ss.hasPermi('config:fileLogInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(FileLogInfoQuery fileLogInfoQuery)
    {
        FileLogInfo fileLogInfo = FileLogInfoQuery.queryToObj(fileLogInfoQuery);
        startPage();
        List<FileLogInfo> list = fileLogInfoService.selectFileLogInfoList(fileLogInfo);
        List<FileLogInfoVo> listVo= list.stream().map(FileLogInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出文件日志列表
     */
    @PreAuthorize("@ss.hasPermi('config:fileLogInfo:export')")
    @Log(title = "文件日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FileLogInfoQuery fileLogInfoQuery)
    {
        FileLogInfo fileLogInfo = FileLogInfoQuery.queryToObj(fileLogInfoQuery);
        List<FileLogInfo> list = fileLogInfoService.selectFileLogInfoList(fileLogInfo);
        ExcelUtil<FileLogInfo> util = new ExcelUtil<FileLogInfo>(FileLogInfo.class);
        util.exportExcel(response, list, "文件日志数据");
    }

    /**
     * 获取文件日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('config:fileLogInfo:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") String logId)
    {
        FileLogInfo fileLogInfo = fileLogInfoService.selectFileLogInfoByLogId(logId);
        return success(FileLogInfoVo.objToVo(fileLogInfo));
    }

    /**
     * 新增文件日志
     */
    @PreAuthorize("@ss.hasPermi('config:fileLogInfo:add')")
    @Log(title = "文件日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FileLogInfoInsert fileLogInfoInsert)
    {
        FileLogInfo fileLogInfo = FileLogInfoInsert.insertToObj(fileLogInfoInsert);
        return toAjax(fileLogInfoService.insertFileLogInfo(fileLogInfo));
    }

    /**
     * 修改文件日志
     */
    @PreAuthorize("@ss.hasPermi('config:fileLogInfo:edit')")
    @Log(title = "文件日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FileLogInfoEdit fileLogInfoEdit)
    {
        FileLogInfo fileLogInfo = FileLogInfoEdit.editToObj(fileLogInfoEdit);
        return toAjax(fileLogInfoService.updateFileLogInfo(fileLogInfo));
    }

    /**
     * 删除文件日志
     */
    @PreAuthorize("@ss.hasPermi('config:fileLogInfo:remove')")
    @Log(title = "文件日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable String[] logIds)
    {
        return toAjax(fileLogInfoService.deleteFileLogInfoByLogIds(logIds));
    }
}
