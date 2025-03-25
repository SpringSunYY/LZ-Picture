package com.lz.ai.controller.admin;

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
import com.lz.ai.model.domain.ConversationLogInfo;
import com.lz.ai.model.vo.conversationLogInfo.ConversationLogInfoVo;
import com.lz.ai.model.dto.conversationLogInfo.ConversationLogInfoQuery;
import com.lz.ai.model.dto.conversationLogInfo.ConversationLogInfoInsert;
import com.lz.ai.model.dto.conversationLogInfo.ConversationLogInfoEdit;
import com.lz.ai.service.IConversationLogInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * AI对话明细记录Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/admin/ai/conversationLogInfo")
public class ConversationLogInfoController extends BaseController
{
    @Resource
    private IConversationLogInfoService conversationLogInfoService;

    /**
     * 查询AI对话明细记录列表
     */
    @PreAuthorize("@ss.hasPermi('ai:conversationLogInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(ConversationLogInfoQuery conversationLogInfoQuery)
    {
        ConversationLogInfo conversationLogInfo = ConversationLogInfoQuery.queryToObj(conversationLogInfoQuery);
        startPage();
        List<ConversationLogInfo> list = conversationLogInfoService.selectConversationLogInfoList(conversationLogInfo);
        List<ConversationLogInfoVo> listVo= list.stream().map(ConversationLogInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出AI对话明细记录列表
     */
    @PreAuthorize("@ss.hasPermi('ai:conversationLogInfo:export')")
    @Log(title = "AI对话明细记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ConversationLogInfoQuery conversationLogInfoQuery)
    {
        ConversationLogInfo conversationLogInfo = ConversationLogInfoQuery.queryToObj(conversationLogInfoQuery);
        List<ConversationLogInfo> list = conversationLogInfoService.selectConversationLogInfoList(conversationLogInfo);
        ExcelUtil<ConversationLogInfo> util = new ExcelUtil<ConversationLogInfo>(ConversationLogInfo.class);
        util.exportExcel(response, list, "AI对话明细记录数据");
    }

    /**
     * 获取AI对话明细记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:conversationLogInfo:query')")
    @GetMapping(value = "/{conversationId}")
    public AjaxResult getInfo(@PathVariable("conversationId") String conversationId)
    {
        ConversationLogInfo conversationLogInfo = conversationLogInfoService.selectConversationLogInfoByConversationId(conversationId);
        return success(ConversationLogInfoVo.objToVo(conversationLogInfo));
    }

    /**
     * 新增AI对话明细记录
     */
    @PreAuthorize("@ss.hasPermi('ai:conversationLogInfo:add')")
    @Log(title = "AI对话明细记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ConversationLogInfoInsert conversationLogInfoInsert)
    {
        ConversationLogInfo conversationLogInfo = ConversationLogInfoInsert.insertToObj(conversationLogInfoInsert);
        return toAjax(conversationLogInfoService.insertConversationLogInfo(conversationLogInfo));
    }

    /**
     * 修改AI对话明细记录
     */
    @PreAuthorize("@ss.hasPermi('ai:conversationLogInfo:edit')")
    @Log(title = "AI对话明细记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ConversationLogInfoEdit conversationLogInfoEdit)
    {
        ConversationLogInfo conversationLogInfo = ConversationLogInfoEdit.editToObj(conversationLogInfoEdit);
        return toAjax(conversationLogInfoService.updateConversationLogInfo(conversationLogInfo));
    }

    /**
     * 删除AI对话明细记录
     */
    @PreAuthorize("@ss.hasPermi('ai:conversationLogInfo:remove')")
    @Log(title = "AI对话明细记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{conversationIds}")
    public AjaxResult remove(@PathVariable String[] conversationIds)
    {
        return toAjax(conversationLogInfoService.deleteConversationLogInfoByConversationIds(conversationIds));
    }
}
