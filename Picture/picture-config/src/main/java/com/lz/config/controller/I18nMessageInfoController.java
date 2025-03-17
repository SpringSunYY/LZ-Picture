package com.lz.config.controller;

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
import com.lz.config.model.domain.I18nMessageInfo;
import com.lz.config.model.vo.i18nMessageInfo.I18nMessageInfoVo;
import com.lz.config.model.dto.i18nMessageInfo.I18nMessageInfoQuery;
import com.lz.config.model.dto.i18nMessageInfo.I18nMessageInfoInsert;
import com.lz.config.model.dto.i18nMessageInfo.I18nMessageInfoEdit;
import com.lz.config.service.II18nMessageInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 国际化信息Controller
 *
 * @author YY
 * @date 2025-02-28
 */
@RestController
@RequestMapping("/config/i18nMessageInfo")
public class I18nMessageInfoController extends BaseController
{
    @Resource
    private II18nMessageInfoService i18nMessageInfoService;

/**
 * 查询国际化信息列表
 */
@PreAuthorize("@ss.hasPermi('config:i18nMessageInfo:list')")
@GetMapping("/list")
    public TableDataInfo list(I18nMessageInfoQuery i18nMessageInfoQuery)
    {
        I18nMessageInfo i18nMessageInfo = I18nMessageInfoQuery.queryToObj(i18nMessageInfoQuery);
        startPage();
        List<I18nMessageInfo> list = i18nMessageInfoService.selectI18nMessageInfoList(i18nMessageInfo);
        List<I18nMessageInfoVo> listVo= list.stream().map(I18nMessageInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出国际化信息列表
     */
    @PreAuthorize("@ss.hasPermi('config:i18nMessageInfo:export')")
    @Log(title = "国际化信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, I18nMessageInfoQuery i18nMessageInfoQuery)
    {
        I18nMessageInfo i18nMessageInfo = I18nMessageInfoQuery.queryToObj(i18nMessageInfoQuery);
        List<I18nMessageInfo> list = i18nMessageInfoService.selectI18nMessageInfoList(i18nMessageInfo);
        ExcelUtil<I18nMessageInfo> util = new ExcelUtil<I18nMessageInfo>(I18nMessageInfo.class);
        util.exportExcel(response, list, "国际化信息数据");
    }

    /**
     * 获取国际化信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('config:i18nMessageInfo:query')")
    @GetMapping(value = "/{messageId}")
    public AjaxResult getInfo(@PathVariable("messageId") Long messageId)
    {
        I18nMessageInfo i18nMessageInfo = i18nMessageInfoService.selectI18nMessageInfoByMessageId(messageId);
        return success(I18nMessageInfoVo.objToVo(i18nMessageInfo));
    }

    /**
     * 新增国际化信息
     */
    @PreAuthorize("@ss.hasPermi('config:i18nMessageInfo:add')")
    @Log(title = "国际化信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody I18nMessageInfoInsert i18nMessageInfoInsert)
    {
        I18nMessageInfo i18nMessageInfo = I18nMessageInfoInsert.insertToObj(i18nMessageInfoInsert);
        return toAjax(i18nMessageInfoService.insertI18nMessageInfo(i18nMessageInfo));
    }

    /**
     * 修改国际化信息
     */
    @PreAuthorize("@ss.hasPermi('config:i18nMessageInfo:edit')")
    @Log(title = "国际化信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody I18nMessageInfoEdit i18nMessageInfoEdit)
    {
        I18nMessageInfo i18nMessageInfo = I18nMessageInfoEdit.editToObj(i18nMessageInfoEdit);
        return toAjax(i18nMessageInfoService.updateI18nMessageInfo(i18nMessageInfo));
    }

    /**
     * 删除国际化信息
     */
    @PreAuthorize("@ss.hasPermi('config:i18nMessageInfo:remove')")
    @Log(title = "国际化信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{messageIds}")
    public AjaxResult remove(@PathVariable Long[] messageIds)
    {
        return toAjax(i18nMessageInfoService.deleteI18nMessageInfoByMessageIds(messageIds));
    }
}
