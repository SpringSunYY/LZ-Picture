package com.lz.picture.model.dto.pictureDownloadLogInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.core.page.PageDomain;
import com.lz.picture.model.domain.PictureDownloadLogInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 图片下载记录Query对象 p_picture_download_log_info
 * 用户查询
 *
 * @author YY
 * @date 2025-05-24
 */
@Data
public class UserPictureDownloadLogInfoQuery extends PageDomain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 图片名称
     */
    private String pictureName;

    /**
     * 下载状态（1失败 0成功）
     */
    private String downloadStatus;

    /**
     * 下载方式（0手动 1API 2批量）
     */
    private String downloadType;

    /**
     * 来源（0其他 1详情 2分享）
     */
    private String referSource;

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
    public static PictureDownloadLogInfo queryToObj(UserPictureDownloadLogInfoQuery pictureDownloadLogInfoQuery) {
        if (pictureDownloadLogInfoQuery == null) {
            return null;
        }
        PictureDownloadLogInfo pictureDownloadLogInfo = new PictureDownloadLogInfo();
        BeanUtils.copyProperties(pictureDownloadLogInfoQuery, pictureDownloadLogInfo);
        return pictureDownloadLogInfo;
    }
}
