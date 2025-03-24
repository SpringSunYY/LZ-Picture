package com.lz.picture.model.dto.searchLogInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.SearchLogInfo;
/**
 * 用户搜索记录Query对象 p_search_log_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class SearchLogInfoQuery implements Serializable
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

    /** 搜索时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

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

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param searchLogInfoQuery 查询对象
     * @return SearchLogInfo
     */
    public static SearchLogInfo queryToObj(SearchLogInfoQuery searchLogInfoQuery) {
        if (searchLogInfoQuery == null) {
            return null;
        }
        SearchLogInfo searchLogInfo = new SearchLogInfo();
        BeanUtils.copyProperties(searchLogInfoQuery, searchLogInfo);
        return searchLogInfo;
    }
}
