package com.lz.picture.model.vo.pictureDownloadLogInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.picture.model.domain.PictureDownloadLogInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 图片下载记录Vo对象 p_picture_download_log_info
 * 用户自己的记录
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
    private String downloadId;

    /**
     * 图片编号
     */
    @Excel(name = "图片编号")
    private String pictureId;

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
     * 消耗积分
     */
    @Excel(name = "消耗积分")
    private Long pointsCost;

    /**
     * 下载状态（1失败 0成功） 字典类型 p_download_status
     */
    @Excel(name = "下载状态", readConverterExp = "1=失败,0=成功")
    private String downloadStatus;

    /**
     * 下载方式（0手动 1API 2批量） 字典类型：p_download_type
     */
    @Excel(name = "下载方式", readConverterExp = "0=手动,1=API,2=批量")
    private String downloadType;

    /**
     * 来源（0其他 1详情 2分享） 字典类型：p_download_refer_source
     */
    @Excel(name = "来源", readConverterExp = "0=其他,1=详情,2=分享")
    private String referSource;

    /**
     * 下载时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "下载时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;



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

    /**
     * 对象封装列表
     * @author: YY
     * @method: objToVo
     * @date: 2025/5/25 23:10
     * @param pictureDownloadLogInfoList PictureDownloadLogInfo实体对象列表
     * @return List<UserPictureDownloadLogInfoVo>
     **/
    public static List<UserPictureDownloadLogInfoVo> objToVo(List<PictureDownloadLogInfo> pictureDownloadLogInfoList) {
        if (pictureDownloadLogInfoList == null) {
            return null;
        }
        return pictureDownloadLogInfoList.stream().map(UserPictureDownloadLogInfoVo::objToVo).toList();
    }
}
