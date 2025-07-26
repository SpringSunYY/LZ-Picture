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
import com.lz.config.model.domain.NoticeInfo;
import com.lz.config.model.vo.noticeInfo.NoticeInfoVo;
import com.lz.config.model.dto.noticeInfo.NoticeInfoQuery;
import com.lz.config.model.dto.noticeInfo.NoticeInfoInsert;
import com.lz.config.model.dto.noticeInfo.NoticeInfoEdit;
import com.lz.config.service.INoticeInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 用户公告Controller
 *
 * @author YY
 * @date 2025-07-26
 */
@RestController
@RequestMapping("/admin/config/noticeInfo")
public class NoticeInfoController extends BaseController
{
    @Resource
    private INoticeInfoService noticeInfoService;

    /**
     * 查询用户公告列表
     */
    @PreAuthorize("@ss.hasPermi('config:noticeInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(NoticeInfoQuery noticeInfoQuery)
    {
        NoticeInfo noticeInfo = NoticeInfoQuery.queryToObj(noticeInfoQuery);
        startPage();
        List<NoticeInfo> list = noticeInfoService.selectNoticeInfoList(noticeInfo);
        List<NoticeInfoVo> listVo= list.stream().map(NoticeInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户公告列表
     */
    @PreAuthorize("@ss.hasPermi('config:noticeInfo:export')")
    @Log(title = "用户公告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NoticeInfoQuery noticeInfoQuery)
    {
        NoticeInfo noticeInfo = NoticeInfoQuery.queryToObj(noticeInfoQuery);
        List<NoticeInfo> list = noticeInfoService.selectNoticeInfoList(noticeInfo);
        ExcelUtil<NoticeInfo> util = new ExcelUtil<NoticeInfo>(NoticeInfo.class);
        util.exportExcel(response, list, "用户公告数据");
    }

    /**
     * 获取用户公告详细信息
     */
    @PreAuthorize("@ss.hasPermi('config:noticeInfo:query')")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable("noticeId") String noticeId)
    {
        NoticeInfo noticeInfo = noticeInfoService.selectNoticeInfoByNoticeId(noticeId);
        return success(NoticeInfoVo.objToVo(noticeInfo));
    }

    /**
     * 新增用户公告
     */
    @PreAuthorize("@ss.hasPermi('config:noticeInfo:add')")
    @Log(title = "用户公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NoticeInfoInsert noticeInfoInsert)
    {
        NoticeInfo noticeInfo = NoticeInfoInsert.insertToObj(noticeInfoInsert);
        return toAjax(noticeInfoService.insertNoticeInfo(noticeInfo));
    }

    /**
     * 修改用户公告
     */
    @PreAuthorize("@ss.hasPermi('config:noticeInfo:edit')")
    @Log(title = "用户公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NoticeInfoEdit noticeInfoEdit)
    {
        NoticeInfo noticeInfo = NoticeInfoEdit.editToObj(noticeInfoEdit);
        return toAjax(noticeInfoService.updateNoticeInfo(noticeInfo));
    }

    /**
     * 删除用户公告
     */
    @PreAuthorize("@ss.hasPermi('config:noticeInfo:remove')")
    @Log(title = "用户公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable String[] noticeIds)
    {
        return toAjax(noticeInfoService.deleteNoticeInfoByNoticeIds(noticeIds));
    }
}
