package com.lz.config.controller.user;

import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.config.model.dto.noticeInfo.NoticeInfoRequest;
import com.lz.config.model.vo.noticeInfo.UserNoticeInfoVo;
import com.lz.config.service.INoticeInfoService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户公告Controller
 *
 * @author YY
 * @date 2025-07-26
 */
@RestController
@RequestMapping("/user/config/noticeInfo")
public class UserNoticeInfoController extends BaseController {
    @Resource
    private INoticeInfoService noticeInfoService;

    /**
     * 查询用户公告列表
     */
    @PreAuthorize("@uss.hasPermi('notice')")
    @GetMapping("/list")
    public TableDataInfo list(NoticeInfoRequest noticeInfoRequest) {
        if (noticeInfoRequest.getPageNum() <= 0) {
            noticeInfoRequest.setPageNum(1);
        }
        if (noticeInfoRequest.getPageSize() > 50) {
            noticeInfoRequest.setPageSize(50);
        }
        return noticeInfoService.selectUserNoticeInfoList(noticeInfoRequest);
    }

    /**
     * 获取公告详情
     *
     * @param noticeId 公告ID
     * @return
     */
    @PreAuthorize("@uss.hasPermi('notice')")
    @GetMapping("/{noticeId}")
    public AjaxResult getInfo(@PathVariable String noticeId) {
        UserNoticeInfoVo noticeInfo = noticeInfoService.selectUserNoticeInfoByNoticeId(noticeId);
        return AjaxResult.success(noticeInfo);
    }

    /**
     * 查询公告必读公告
     *
     * @param noticeInfoRequest 请求
     * @return
     */
    @GetMapping("/exhibit")
    public AjaxResult exhibit(NoticeInfoRequest noticeInfoRequest) {
        UserNoticeInfoVo noticeInfo = noticeInfoService.selectUserNoticeInfoByExhibit(noticeInfoRequest);
        return AjaxResult.success(noticeInfo);
    }
}
