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
import com.lz.config.model.domain.I18nKeyInfo;
import com.lz.config.model.vo.i18nKeyInfo.I18nKeyInfoVo;
import com.lz.config.model.dto.i18nKeyInfo.I18nKeyInfoQuery;
import com.lz.config.model.dto.i18nKeyInfo.I18nKeyInfoInsert;
import com.lz.config.model.dto.i18nKeyInfo.I18nKeyInfoEdit;
import com.lz.config.service.II18nKeyInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 国际化键名Controller
 *
 * @author YY
 * @date 2025-02-28
 */
@RestController
@RequestMapping("/config/i18nKeyInfo")
public class I18nKeyInfoController extends BaseController
{
    @Resource
    private II18nKeyInfoService i18nKeyInfoService;

/**
 * 查询国际化键名列表
 */
@PreAuthorize("@ss.hasPermi('config:i18nKeyInfo:list')")
@GetMapping("/list")
    public TableDataInfo list(I18nKeyInfoQuery i18nKeyInfoQuery)
    {
        I18nKeyInfo i18nKeyInfo = I18nKeyInfoQuery.queryToObj(i18nKeyInfoQuery);
        startPage();
        List<I18nKeyInfo> list = i18nKeyInfoService.selectI18nKeyInfoList(i18nKeyInfo);
        List<I18nKeyInfoVo> listVo= list.stream().map(I18nKeyInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出国际化键名列表
     */
    @PreAuthorize("@ss.hasPermi('config:i18nKeyInfo:export')")
    @Log(title = "国际化键名", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, I18nKeyInfoQuery i18nKeyInfoQuery)
    {
        I18nKeyInfo i18nKeyInfo = I18nKeyInfoQuery.queryToObj(i18nKeyInfoQuery);
        List<I18nKeyInfo> list = i18nKeyInfoService.selectI18nKeyInfoList(i18nKeyInfo);
        ExcelUtil<I18nKeyInfo> util = new ExcelUtil<I18nKeyInfo>(I18nKeyInfo.class);
        util.exportExcel(response, list, "国际化键名数据");
    }

    /**
     * 获取国际化键名详细信息
     */
    @PreAuthorize("@ss.hasPermi('config:i18nKeyInfo:query')")
    @GetMapping(value = "/{keyId}")
    public AjaxResult getInfo(@PathVariable("keyId") Long keyId)
    {
        I18nKeyInfo i18nKeyInfo = i18nKeyInfoService.selectI18nKeyInfoByKeyId(keyId);
        return success(I18nKeyInfoVo.objToVo(i18nKeyInfo));
    }

    /**
     * 新增国际化键名
     */
    @PreAuthorize("@ss.hasPermi('config:i18nKeyInfo:add')")
    @Log(title = "国际化键名", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody I18nKeyInfoInsert i18nKeyInfoInsert)
    {
        I18nKeyInfo i18nKeyInfo = I18nKeyInfoInsert.insertToObj(i18nKeyInfoInsert);
        return toAjax(i18nKeyInfoService.insertI18nKeyInfo(i18nKeyInfo));
    }

    /**
     * 修改国际化键名
     */
    @PreAuthorize("@ss.hasPermi('config:i18nKeyInfo:edit')")
    @Log(title = "国际化键名", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody I18nKeyInfoEdit i18nKeyInfoEdit)
    {
        I18nKeyInfo i18nKeyInfo = I18nKeyInfoEdit.editToObj(i18nKeyInfoEdit);
        return toAjax(i18nKeyInfoService.updateI18nKeyInfo(i18nKeyInfo));
    }

    /**
     * 删除国际化键名
     */
    @PreAuthorize("@ss.hasPermi('config:i18nKeyInfo:remove')")
    @Log(title = "国际化键名", businessType = BusinessType.DELETE)
    @DeleteMapping("/{keyIds}")
    public AjaxResult remove(@PathVariable Long[] keyIds)
    {
        return toAjax(i18nKeyInfoService.deleteI18nKeyInfoByKeyIds(keyIds));
    }
}
