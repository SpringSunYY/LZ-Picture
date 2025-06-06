package com.lz.picture.model.vo.pictureDownloadLogInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.picture.model.domain.PictureDownloadLogInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 图片下载记录Vo对象 p_picture_download_log_info
 *
 * @author YY
 * @date 2025-06-06
 */
@Data
public class PictureDownloadLogInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 下载编号
     */
    @Excel(name = "下载编号")
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
     * 图片标签
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
    private BigDecimal authorProportion;

    /**
     * 官方分成比例
     */
    @Excel(name = "官方分成比例")
    private BigDecimal officialProportion;

    /**
     * 空间分成比例
     */
    @Excel(name = "空间分成比例")
    private BigDecimal spaceProportion;

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
     * 下载类型（0查看 1下载 2批量下载
     */
    @Excel(name = "下载类型", readConverterExp = "下载类型（0查看 1下载 2批量下载")
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
     * 分数
     */
    @Excel(name = "分数")
    private BigDecimal score;

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
     * 对象转封装类
     *
     * @param pictureDownloadLogInfo PictureDownloadLogInfo实体对象
     * @return PictureDownloadLogInfoVo
     */
    public static PictureDownloadLogInfoVo objToVo(PictureDownloadLogInfo pictureDownloadLogInfo) {
        if (pictureDownloadLogInfo == null) {
            return null;
        }
        PictureDownloadLogInfoVo pictureDownloadLogInfoVo = new PictureDownloadLogInfoVo();
        BeanUtils.copyProperties(pictureDownloadLogInfo, pictureDownloadLogInfoVo);
        return pictureDownloadLogInfoVo;
    }
}
