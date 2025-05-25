package com.lz.picture.model.dto.pictureDownloadLogInfo;

import java.io.Serial;
import java.io.Serializable;

import com.lz.common.annotation.Excel;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.PictureDownloadLogInfo;
/**
 * 图片下载记录Vo对象 p_picture_download_log_info
 *
 * @author YY
 * @date 2025-05-24
 */
@Data
public class PictureDownloadLogInfoEdit implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    /** 下载编号 */
    private String downloadId;

    /** 用户编号 */
    private String userId;

    /** 图片编号 */
    private String pictureId;

    /** 图片分类 */
    private String categoryId;

    /** 是否免费（0是 1否） */
    private String isFree;

    /**
     * 是否统计（0否 1是）
     */
    private String hasStatistics;

    /**
     * 对象转封装类
     *
     * @param pictureDownloadLogInfoEdit 编辑对象
     * @return PictureDownloadLogInfo
     */
    public static PictureDownloadLogInfo editToObj(PictureDownloadLogInfoEdit pictureDownloadLogInfoEdit) {
        if (pictureDownloadLogInfoEdit == null) {
            return null;
        }
        PictureDownloadLogInfo pictureDownloadLogInfo = new PictureDownloadLogInfo();
        BeanUtils.copyProperties(pictureDownloadLogInfoEdit, pictureDownloadLogInfo);
        return pictureDownloadLogInfo;
    }
}
