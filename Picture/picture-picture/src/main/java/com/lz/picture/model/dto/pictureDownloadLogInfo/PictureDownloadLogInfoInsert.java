package com.lz.picture.model.dto.pictureDownloadLogInfo;

import com.lz.picture.model.domain.PictureDownloadLogInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 图片下载记录Vo对象 p_picture_download_log_info
 *
 * @author YY
 * @date 2025-06-06
 */
@Data
public class PictureDownloadLogInfoInsert implements Serializable {
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
     * 缩略图URL
     */
    private String thumbnailUrl;

    /**
     * 图片标签
     */
    private String tags;

    /**
     * 空间编号
     */
    private String spaceId;

    /**
     * 下载状态（1失败 0成功）
     */
    private String downloadStatus;

    /**
     * 失败原因
     */
    private String failReason;

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
     * 对象转封装类
     *
     * @param pictureDownloadLogInfoInsert 插入对象
     * @return PictureDownloadLogInfoInsert
     */
    public static PictureDownloadLogInfo insertToObj(PictureDownloadLogInfoInsert pictureDownloadLogInfoInsert) {
        if (pictureDownloadLogInfoInsert == null) {
            return null;
        }
        PictureDownloadLogInfo pictureDownloadLogInfo = new PictureDownloadLogInfo();
        BeanUtils.copyProperties(pictureDownloadLogInfoInsert, pictureDownloadLogInfo);
        return pictureDownloadLogInfo;
    }
}
