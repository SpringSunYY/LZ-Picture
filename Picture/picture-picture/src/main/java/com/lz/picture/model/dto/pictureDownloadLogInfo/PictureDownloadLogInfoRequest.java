package com.lz.picture.model.dto.pictureDownloadLogInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.picture.model.domain.PictureDownloadLogInfo;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 图片下载记录Query对象 p_picture_download_log_info
 * 用户自己通过记录下载
 *
 * @author YY
 * @date 2025-05-24
 */
@Data
public class PictureDownloadLogInfoRequest implements Serializable {
    @Serial
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
     * 对象转封装类
     *
     * @param pictureDownloadLogInfoQuery 查询对象
     * @return PictureDownloadLogInfo
     */
    public static PictureDownloadLogInfo queryToObj(PictureDownloadLogInfoRequest pictureDownloadLogInfoQuery) {
        if (pictureDownloadLogInfoQuery == null) {
            return null;
        }
        PictureDownloadLogInfo pictureDownloadLogInfo = new PictureDownloadLogInfo();
        BeanUtils.copyProperties(pictureDownloadLogInfoQuery, pictureDownloadLogInfo);
        return pictureDownloadLogInfo;
    }
}
