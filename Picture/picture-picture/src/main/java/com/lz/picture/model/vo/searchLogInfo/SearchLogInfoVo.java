package com.lz.picture.model.vo.searchLogInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.SearchLogInfo;

/**
 * 用户搜索记录Vo对象 p_search_log_info
 *
 * @author YY
 * @date 2025-06-03
 */
@Data
public class SearchLogInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 搜索记录编号
     */
    @Excel(name = "搜索记录编号")
    private String searchId;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private String userId;

    /**
     * 搜索关键词
     */
    @Excel(name = "搜索关键词")
    private String keyword;

    /**
     * 搜索类型（0图片 1空间 2用户）
     */
    @Excel(name = "搜索类型", readConverterExp = "0=图片,1=空间,2=用户")
    private String searchType;

    /**
     * 搜索来源（0首页 1推荐 2搜索页 3AI推荐 4历史搜索）
     */
    @Excel(name = "搜索来源", readConverterExp = "0=首页,1=推荐,2=搜索页,3=AI推荐,4=历史搜索")
    private String referSource;

    /**
     * 搜索状态（0成功 1失败）
     */
    @Excel(name = "搜索状态", readConverterExp = "0=成功,1=失败")
    private String searchStatus;

    /**
     * 失败原因
     */
    @Excel(name = "失败原因")
    private String failReason;

    /**
     * 返回数量
     */
    @Excel(name = "返回数量")
    private Long resultCount;

    /**
     * 搜索时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "搜索时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 搜索时长（毫秒）
     */
    @Excel(name = "搜索时长", readConverterExp = "毫=秒")
    private Long searchDuration;

    /**
     * 设备唯一标识
     */
    @Excel(name = "设备唯一标识")
    private String deviceId;

    /**
     * 浏览器类型
     */
    @Excel(name = "浏览器类型")
    private String browser;

    /**
     * 操作系统
     */
    @Excel(name = "操作系统")
    private String os;

    /**
     * 平台
     */
    @Excel(name = "平台")
    private String platform;

    /**
     * IP属地
     */
    @Excel(name = "IP属地")
    private String ipAddress;

    /**
     * IP地址
     */
    @Excel(name = "IP地址")
    private String ipAddr;


    /**
     * 对象转封装类
     *
     * @param searchLogInfo SearchLogInfo实体对象
     * @return SearchLogInfoVo
     */
    public static SearchLogInfoVo objToVo(SearchLogInfo searchLogInfo) {
        if (searchLogInfo == null) {
            return null;
        }
        SearchLogInfoVo searchLogInfoVo = new SearchLogInfoVo();
        BeanUtils.copyProperties(searchLogInfo, searchLogInfoVo);
        return searchLogInfoVo;
    }
}
