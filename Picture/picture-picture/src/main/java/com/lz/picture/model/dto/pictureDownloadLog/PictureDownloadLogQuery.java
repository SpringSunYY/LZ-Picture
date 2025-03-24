package com.lz.picture.model.dto.pictureDownloadLog;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureDownloadLog;
/**
 * 图片下载记录Query对象 p_picture_download_log
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureDownloadLogQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 下载编号 */
    private String downloadId;

    /** 用户编号 */
    private String userId;

    /** 图片编号 */
    private String pictureId;

    /** 图片分类 */
    private String categoryId;

    /** 图片标签（格式："标签1","标签2"） */
    private String tags;

    /** 空间编号 */
    private String spaceId;

    /** 下载IP地址 */
    private String downloadIp;

    /** 设备唯一标识 */
    private String deviceId;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 平台（Web/APP） */
    private String platform;

    /** 消耗积分 */
    private Long pointsCost;

    /** 是否免费（0是 1否） */
    private String isFree;

    /** 分成比例（如0.3表示30%） */
    private Long proportion;

    /** 下载时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 下载状态（1失败 0成功） */
    private String downloadStatus;

    /** 下载方式（0手动 1API 2批量） */
    private String downloadType;

    /** 来源（0其他 1详情 2分享） */
    private String referSource;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param pictureDownloadLogQuery 查询对象
     * @return PictureDownloadLog
     */
    public static PictureDownloadLog queryToObj(PictureDownloadLogQuery pictureDownloadLogQuery) {
        if (pictureDownloadLogQuery == null) {
            return null;
        }
        PictureDownloadLog pictureDownloadLog = new PictureDownloadLog();
        BeanUtils.copyProperties(pictureDownloadLogQuery, pictureDownloadLog);
        return pictureDownloadLog;
    }
}
