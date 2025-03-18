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
import com.lz.config.model.domain.I18nLocaleInfo;
import com.lz.config.model.vo.i18nLocaleInfo.I18nLocaleInfoVo;
import com.lz.config.model.dto.i18nLocaleInfo.I18nLocaleInfoQuery;
import com.lz.config.model.dto.i18nLocaleInfo.I18nLocaleInfoInsert;
import com.lz.config.model.dto.i18nLocaleInfo.I18nLocaleInfoEdit;
import com.lz.config.service.II18nLocaleInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 国际化国家Controller
 *
 * @author YY
 * @date 2025-02-28
 */
@RestController
@RequestMapping("/admin/config/i18nLocaleInfo")
public class I18nLocaleInfoController extends BaseController
{
    @Resource
    private II18nLocaleInfoService i18nLocaleInfoService;

/**
 * 查询国际化国家列表
 */
@PreAuthorize("@ss.hasPermi('config:i18nLocaleInfo:list')")
@GetMapping("/list")
    public TableDataInfo list(I18nLocaleInfoQuery i18nLocaleInfoQuery)
    {
        I18nLocaleInfo i18nLocaleInfo = I18nLocaleInfoQuery.queryToObj(i18nLocaleInfoQuery);
        startPage();
        List<I18nLocaleInfo> list = i18nLocaleInfoService.selectI18nLocaleInfoList(i18nLocaleInfo);
        List<I18nLocaleInfoVo> listVo= list.stream().map(I18nLocaleInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出国际化国家列表
     */
    @PreAuthorize("@ss.hasPermi('config:i18nLocaleInfo:export')")
    @Log(title = "国际化国家", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, I18nLocaleInfoQuery i18nLocaleInfoQuery)
    {
        I18nLocaleInfo i18nLocaleInfo = I18nLocaleInfoQuery.queryToObj(i18nLocaleInfoQuery);
        List<I18nLocaleInfo> list = i18nLocaleInfoService.selectI18nLocaleInfoList(i18nLocaleInfo);
        ExcelUtil<I18nLocaleInfo> util = new ExcelUtil<I18nLocaleInfo>(I18nLocaleInfo.class);
        util.exportExcel(response, list, "国际化国家数据");
    }

    /**
     * 获取国际化国家详细信息
     */
    @PreAuthorize("@ss.hasPermi('config:i18nLocaleInfo:query')")
    @GetMapping(value = "/{localeId}")
    public AjaxResult getInfo(@PathVariable("localeId") Long localeId)
    {
        I18nLocaleInfo i18nLocaleInfo = i18nLocaleInfoService.selectI18nLocaleInfoByLocaleId(localeId);
        return success(I18nLocaleInfoVo.objToVo(i18nLocaleInfo));
    }

    /**
     * 新增国际化国家
     */
    @PreAuthorize("@ss.hasPermi('config:i18nLocaleInfo:add')")
    @Log(title = "国际化国家", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody I18nLocaleInfoInsert i18nLocaleInfoInsert)
    {
        I18nLocaleInfo i18nLocaleInfo = I18nLocaleInfoInsert.insertToObj(i18nLocaleInfoInsert);
        return toAjax(i18nLocaleInfoService.insertI18nLocaleInfo(i18nLocaleInfo));
    }

    /**
     * 修改国际化国家
     */
    @PreAuthorize("@ss.hasPermi('config:i18nLocaleInfo:edit')")
    @Log(title = "国际化国家", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody I18nLocaleInfoEdit i18nLocaleInfoEdit)
    {
        I18nLocaleInfo i18nLocaleInfo = I18nLocaleInfoEdit.editToObj(i18nLocaleInfoEdit);
        return toAjax(i18nLocaleInfoService.updateI18nLocaleInfo(i18nLocaleInfo));
    }

    /**
     * 删除国际化国家
     */
    @PreAuthorize("@ss.hasPermi('config:i18nLocaleInfo:remove')")
    @Log(title = "国际化国家", businessType = BusinessType.DELETE)
    @DeleteMapping("/{localeIds}")
    public AjaxResult remove(@PathVariable Long[] localeIds)
    {
        return toAjax(i18nLocaleInfoService.deleteI18nLocaleInfoByLocaleIds(localeIds));
    }
}
