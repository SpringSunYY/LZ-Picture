package com.lz.picture.model.vo.pictureDownloadLogInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.picture.model.domain.PictureDownloadLogInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 图片下载记录Vo对象 p_picture_download_log_info
 *
 * @author YY
 * @date 2025-05-24
 */
@Data
public class UserPictureDownloadLogInfoVo implements Serializable {
    @Serial
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


    /** 图片名称 */
    @Excel(name = "图片名称")
    private String pictureName;

    /** 缩略图URL */
    @Excel(name = "缩略图URL")
    private String thumbnailUrl;


    /**
     * 图片标签（格式："标签1","标签2"）
     */
    @Excel(name = "图片标签")
    private String tags;


    /**
     * 消耗积分
     */
    @Excel(name = "消耗积分")
    private Long pointsCost;



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
     * 对象转封装类
     *
     * @param pictureDownloadLogInfo PictureDownloadLogInfo实体对象
     * @return PictureDownloadLogInfoVo
     */
    public static UserPictureDownloadLogInfoVo objToVo(PictureDownloadLogInfo pictureDownloadLogInfo) {
        if (pictureDownloadLogInfo == null) {
            return null;
        }
        UserPictureDownloadLogInfoVo pictureDownloadLogInfoVo = new UserPictureDownloadLogInfoVo();
        BeanUtils.copyProperties(pictureDownloadLogInfo, pictureDownloadLogInfoVo);
        return pictureDownloadLogInfoVo;
    }
}
