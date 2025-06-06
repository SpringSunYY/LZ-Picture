package com.lz.picture.model.dto.pictureDownloadLogInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.picture.model.domain.PictureDownloadLogInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 图片下载记录Query对象 p_picture_download_log_info
 *
 * @author YY
 * @date 2025-06-06
 */
@Data
public class PictureDownloadLogInfoQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 下载编号
     */
    private String downloadId;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 图片编号
     */
    private String pictureId;

    /**
     * 图片分类
     */
    private String categoryId;

    /**
     * 图片名称
     */
    private String pictureName;

    /**
     * 图片标签
     */
    private String tags;

    /**
     * 空间编号
     */
    private String spaceId;

    /**
     * 下载时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 下载状态（1失败 0成功）
     */
    private String downloadStatus;

    /**
     * 下载类型（0查看 1下载 2批量下载
     */
    private String downloadType;

    /**
     * 来源（0其他 1详情 2分享）
     */
    private String referSource;

    /**
     * 是否统计（0否 1是）
     */
    private String hasStatistics;

    /**
     * 分数
     */
    private Double score;

    /**
     * IP地址
     */
    private String ipAddr;

    /**
     * IP属地
     */
    private String ipAddress;

    /**
     * 设备唯一标识
     */
    private String deviceId;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 平台
     */
    private String platform;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param pictureDownloadLogInfoQuery 查询对象
     * @return PictureDownloadLogInfo
     */
    public static PictureDownloadLogInfo queryToObj(PictureDownloadLogInfoQuery pictureDownloadLogInfoQuery) {
        if (pictureDownloadLogInfoQuery == null) {
            return null;
        }
        PictureDownloadLogInfo pictureDownloadLogInfo = new PictureDownloadLogInfo();
        BeanUtils.copyProperties(pictureDownloadLogInfoQuery, pictureDownloadLogInfo);
        return pictureDownloadLogInfo;
    }
}
