package com.lz.config.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.config.model.dto.informTemplateInfo.InformTemplateInfoHistory;
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
import com.lz.config.model.domain.InformTemplateInfo;
import com.lz.config.model.vo.informTemplateInfo.InformTemplateInfoVo;
import com.lz.config.model.dto.informTemplateInfo.InformTemplateInfoQuery;
import com.lz.config.model.dto.informTemplateInfo.InformTemplateInfoInsert;
import com.lz.config.model.dto.informTemplateInfo.InformTemplateInfoEdit;
import com.lz.config.service.IInformTemplateInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 通知模版Controller
 *
 * @author ruoyi
 * @date 2025-03-14
 */
@RestController
@RequestMapping("/config/informTemplateInfo")
public class InformTemplateInfoController extends BaseController {
    @Resource
    private IInformTemplateInfoService informTemplateInfoService;

    /**
     * 查询通知模版列表
     */
    @PreAuthorize("@ss.hasPermi('config:informTemplateInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(InformTemplateInfoQuery informTemplateInfoQuery) {
        InformTemplateInfo informTemplateInfo = InformTemplateInfoQuery.queryToObj(informTemplateInfoQuery);
        startPage();
        List<InformTemplateInfo> list = informTemplateInfoService.selectInformTemplateInfoList(informTemplateInfo);
        List<InformTemplateInfoVo> listVo = list.stream().map(InformTemplateInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出通知模版列表
     */
    @PreAuthorize("@ss.hasPermi('config:informTemplateInfo:export')")
    @Log(title = "通知模版", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, InformTemplateInfoQuery informTemplateInfoQuery) {
        InformTemplateInfo informTemplateInfo = InformTemplateInfoQuery.queryToObj(informTemplateInfoQuery);
        List<InformTemplateInfo> list = informTemplateInfoService.selectInformTemplateInfoList(informTemplateInfo);
        ExcelUtil<InformTemplateInfo> util = new ExcelUtil<InformTemplateInfo>(InformTemplateInfo.class);
        util.exportExcel(response, list, "通知模版数据");
    }

    /**
     * 获取通知模版详细信息
     */
    @PreAuthorize("@ss.hasPermi('config:informTemplateInfo:query')")
    @GetMapping(value = "/{templateId}")
    public AjaxResult getInfo(@PathVariable("templateId") Long templateId) {
        InformTemplateInfo informTemplateInfo = informTemplateInfoService.selectInformTemplateInfoByTemplateId(templateId);
        return success(InformTemplateInfoVo.objToVo(informTemplateInfo));
    }

    /**
     * 新增通知模版
     */
    @PreAuthorize("@ss.hasPermi('config:informTemplateInfo:add')")
    @Log(title = "通知模版", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InformTemplateInfoInsert informTemplateInfoInsert) {
        InformTemplateInfo informTemplateInfo = InformTemplateInfoInsert.insertToObj(informTemplateInfoInsert);
        String example = informTemplateInfoService.getExample(informTemplateInfo);
        informTemplateInfo.setExample(example);
        return toAjax(informTemplateInfoService.insertInformTemplateInfo(informTemplateInfo));
    }

    @PreAuthorize("@ss.hasPermi('config:informTemplateInfo:add')")
    @PostMapping("/getExample")
    public AjaxResult getExample(@RequestBody InformTemplateInfoInsert informTemplateInfoInsert) {
        InformTemplateInfo informTemplateInfo = InformTemplateInfoInsert.insertToObj(informTemplateInfoInsert);
        return success(informTemplateInfoService.getExample(informTemplateInfo));
    }

    /**
     * 修改通知模版
     */
    @PreAuthorize("@ss.hasPermi('config:informTemplateInfo:edit')")
    @Log(title = "通知模版", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InformTemplateInfoEdit informTemplateInfoEdit) {
        //判断是否需要更新版本
        InformTemplateInfo informTemplateInfo = InformTemplateInfoEdit.editToObj(informTemplateInfoEdit);
        informTemplateInfo.setExample(informTemplateInfoService.getExample(informTemplateInfo));
        informTemplateInfo.setUpdateBy(SecurityUtils.getUsername());
        informTemplateInfo.setUpdateTime(DateUtils.getNowDate());
        if (informTemplateInfoEdit.getSaveVersion()) {
            InformTemplateInfo info = informTemplateInfoService.selectInformTemplateInfoByTemplateId(informTemplateInfoEdit.getTemplateId());
            informTemplateInfo.setTemplateVersion(info.getTemplateVersion() + 1);
            //获取到历史记录转化为map
            String templateVersionHistory = info.getTemplateVersionHistory();
            if (StringUtils.isNotEmpty(templateVersionHistory)) {
                HashMap<Long, String> parse = (HashMap<Long, String>) JSONObject.parseObject(templateVersionHistory, HashMap.class);
                parse.put(informTemplateInfo.getTemplateVersion(), InformTemplateInfoHistory.objToHistory(informTemplateInfo).toString());
                informTemplateInfo.setTemplateVersionHistory(JSONObject.toJSONString(parse));
            }
        }
        return toAjax(informTemplateInfoService.updateInformTemplateInfo(informTemplateInfo));
    }

    /**
     * 删除通知模版
     */
    @PreAuthorize("@ss.hasPermi('config:informTemplateInfo:remove')")
    @Log(title = "通知模版", businessType = BusinessType.DELETE)
    @DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable Long[] templateIds) {
        return toAjax(informTemplateInfoService.deleteInformTemplateInfoByTemplateIds(templateIds));
    }
}
