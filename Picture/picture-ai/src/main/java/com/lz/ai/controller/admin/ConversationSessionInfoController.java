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
import com.lz.ai.model.domain.ConversationSessionInfo;
import com.lz.ai.model.vo.conversationSessionInfo.ConversationSessionInfoVo;
import com.lz.ai.model.dto.conversationSessionInfo.ConversationSessionInfoQuery;
import com.lz.ai.model.dto.conversationSessionInfo.ConversationSessionInfoInsert;
import com.lz.ai.model.dto.conversationSessionInfo.ConversationSessionInfoEdit;
import com.lz.ai.service.IConversationSessionInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * AI会话管理Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/admin/ai/conversationSessionInfo")
public class ConversationSessionInfoController extends BaseController
{
    @Resource
    private IConversationSessionInfoService conversationSessionInfoService;

    /**
     * 查询AI会话管理列表
     */
    @PreAuthorize("@ss.hasPermi('ai:conversationSessionInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(ConversationSessionInfoQuery conversationSessionInfoQuery)
    {
        ConversationSessionInfo conversationSessionInfo = ConversationSessionInfoQuery.queryToObj(conversationSessionInfoQuery);
        startPage();
        List<ConversationSessionInfo> list = conversationSessionInfoService.selectConversationSessionInfoList(conversationSessionInfo);
        List<ConversationSessionInfoVo> listVo= list.stream().map(ConversationSessionInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出AI会话管理列表
     */
    @PreAuthorize("@ss.hasPermi('ai:conversationSessionInfo:export')")
    @Log(title = "AI会话管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ConversationSessionInfoQuery conversationSessionInfoQuery)
    {
        ConversationSessionInfo conversationSessionInfo = ConversationSessionInfoQuery.queryToObj(conversationSessionInfoQuery);
        List<ConversationSessionInfo> list = conversationSessionInfoService.selectConversationSessionInfoList(conversationSessionInfo);
        ExcelUtil<ConversationSessionInfo> util = new ExcelUtil<ConversationSessionInfo>(ConversationSessionInfo.class);
        util.exportExcel(response, list, "AI会话管理数据");
    }

    /**
     * 获取AI会话管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:conversationSessionInfo:query')")
    @GetMapping(value = "/{sessionId}")
    public AjaxResult getInfo(@PathVariable("sessionId") String sessionId)
    {
        ConversationSessionInfo conversationSessionInfo = conversationSessionInfoService.selectConversationSessionInfoBySessionId(sessionId);
        return success(ConversationSessionInfoVo.objToVo(conversationSessionInfo));
    }

    /**
     * 新增AI会话管理
     */
    @PreAuthorize("@ss.hasPermi('ai:conversationSessionInfo:add')")
    @Log(title = "AI会话管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ConversationSessionInfoInsert conversationSessionInfoInsert)
    {
        ConversationSessionInfo conversationSessionInfo = ConversationSessionInfoInsert.insertToObj(conversationSessionInfoInsert);
        return toAjax(conversationSessionInfoService.insertConversationSessionInfo(conversationSessionInfo));
    }

    /**
     * 修改AI会话管理
     */
    @PreAuthorize("@ss.hasPermi('ai:conversationSessionInfo:edit')")
    @Log(title = "AI会话管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ConversationSessionInfoEdit conversationSessionInfoEdit)
    {
        ConversationSessionInfo conversationSessionInfo = ConversationSessionInfoEdit.editToObj(conversationSessionInfoEdit);
        return toAjax(conversationSessionInfoService.updateConversationSessionInfo(conversationSessionInfo));
    }

    /**
     * 删除AI会话管理
     */
    @PreAuthorize("@ss.hasPermi('ai:conversationSessionInfo:remove')")
    @Log(title = "AI会话管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sessionIds}")
    public AjaxResult remove(@PathVariable String[] sessionIds)
    {
        return toAjax(conversationSessionInfoService.deleteConversationSessionInfoBySessionIds(sessionIds));
    }
}
