package com.lz.picture.model.dto.searchLogInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.SearchLogInfo;
/**
 * 用户搜索记录Vo对象 p_search_log_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class SearchLogInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 搜索记录编号 */
    private String searchId;

    /** 用户编号 */
    private String userId;

    /** 搜索关键词 */
    private String keyword;

    /** 搜索类型（0图片 1空间 2用户） */
    private String searchType;

    /** 搜索来源（0首页 1推荐 2搜索页 3AI推荐 4历史搜索） */
    private String referSource;

    /** 搜索状态（0成功 1失败） */
    private String searchStatus;

    /** 失败原因 */
    private String failReason;

    /** 返回数量 */
    private Long resultCount;

    /** 搜索时长（毫秒） */
    private Long searchDuration;

    /** 设备唯一标识 */
    private String deviceId;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 平台 */
    private String platform;

    /** IP属地 */
    private String ipAddress;

    /** 点击次数 */
    private Long clickCount;

    /** 收藏数 */
    private Long favoriteCount;

    /** 下载数 */
    private Long downloadCount;

    /**
     * 对象转封装类
     *
     * @param searchLogInfoEdit 编辑对象
     * @return SearchLogInfo
     */
    public static SearchLogInfo editToObj(SearchLogInfoEdit searchLogInfoEdit) {
        if (searchLogInfoEdit == null) {
            return null;
        }
        SearchLogInfo searchLogInfo = new SearchLogInfo();
        BeanUtils.copyProperties(searchLogInfoEdit, searchLogInfo);
        return searchLogInfo;
    }
}
