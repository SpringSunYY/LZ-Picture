package com.lz.picture.model.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.lz.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 图片下载记录对象 p_picture_download_log_info
 *
 * @author YY
 * @date 2025-05-24
 */
@TableName("p_picture_download_log_info")
@Data
public class PictureDownloadLogInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 下载编号
     */
    @Excel(name = "下载编号")
    @TableId(value = "download_id", type = IdType.ASSIGN_ID)
    private String downloadId;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private String userId;

    /**
     * 图片编号
     */
    @Excel(name = "图片编号")
    private String pictureId;

    /**
     * 图片分类
     */
    @Excel(name = "图片分类")
    private String categoryId;

    /**
     * 图片名称
     */
    @Excel(name = "图片名称")
    private String pictureName;

    /**
     * 缩略图URL
     */
    @Excel(name = "缩略图URL")
    private String thumbnailUrl;

    /**
     * 图片标签（格式："标签1","标签2"）
     */
    @Excel(name = "图片标签")
    private String tags;

    /**
     * 空间编号
     */
    @Excel(name = "空间编号")
    private String spaceId;

    /**
     * 消耗积分
     */
    @Excel(name = "消耗积分")
    private Long pointsCost;

    /**
     * 是否免费（0是 1否）
     */
    @Excel(name = "是否免费", readConverterExp = "0=是,1=否")
    private String isFree;

    /**
     * 作者分成积分
     */
    @Excel(name = "作者分成积分")
    private Long pointsAuthorGain;

    /**
     * 平台分成积分
     */
    @Excel(name = "平台分成积分")
    private Long pointsOfficialGain;

    /**
     * 空间分成积分
     */
    @Excel(name = "空间分成积分")
    private Long pointsSpaceGain;

    /**
     * 作者分成比例
     */
    @Excel(name = "作者分成比例")
    private Double authorProportion;


    /**
     * 官方分成比例
     */
    @Excel(name = "官方分成比例")
    private Double officialProportion;

    /**
     * 空间分成比例
     */
    @Excel(name = "空间分成比例")
    private Double spaceProportion;

    /**
     * 下载时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "下载时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 下载状态（1失败 0成功）
     */
    @Excel(name = "下载状态", readConverterExp = "1=失败,0=成功")
    private String downloadStatus;

    /**
     * 失败原因
     */
    @Excel(name = "失败原因")
    private String failReason;

    /**
     * 下载方式（0手动 1API 2批量）
     */
    @Excel(name = "下载方式", readConverterExp = "0=手动,1=API,2=批量")
    private String downloadType;

    /**
     * 来源（0其他 1详情 2分享）
     */
    @Excel(name = "来源", readConverterExp = "0=其他,1=详情,2=分享")
    private String referSource;

    /**
     * 是否统计（0否 1是）
     */
    @Excel(name = "是否统计", readConverterExp = "0=否,1=是")
    private String hasStatistics;

    /**
     * IP地址
     */
    @Excel(name = "IP地址")
    private String ipAddr;

    /**
     * IP属地
     */
    @Excel(name = "IP属地")
    private String ipAddress;

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
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
