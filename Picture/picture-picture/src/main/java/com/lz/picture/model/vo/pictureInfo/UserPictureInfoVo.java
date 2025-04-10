package com.lz.picture.model.vo.pictureInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.picture.model.domain.PictureInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 图片信息Vo对象 p_picture_info
 *
 * @author YY
 * @date 2025-04-09
 */
@Data
public class UserPictureInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片编号
     */
    private String pictureId;

    /**
     * 图片名称
     */
    private String name;


    /**
     * 分类编号
     */
    @Excel(name = "分类编号")
    private String categoryId;


    /**
     * 图片宽度
     */
    @Excel(name = "图片宽度")
    private Long picWidth;

    /**
     * 图片高度
     */
    @Excel(name = "图片高度")
    private Long picHeight;

    /**
     * 宽高比例
     */
    private Double picScale;

    /**
     * 上传用户编号
     */
    @Excel(name = "上传用户编号")
    private String userId;


    /**
     * 缩略图URL
     */
    @Excel(name = "缩略图URL")
    private String thumbnailUrl;

    /**
     * 所属空间编号
     */
    @Excel(name = "所属空间编号")
    private String spaceId;

    /**
     * 所属文件夹编号
     */
    @Excel(name = "所属文件夹编号")
    private String folderId;


    /**
     * 对象转封装类
     *
     * @param pictureInfo PictureInfo实体对象
     * @return PictureInfoVo
     */
    public static UserPictureInfoVo objToVo(PictureInfo pictureInfo) {
        if (pictureInfo == null) {
            return null;
        }
        UserPictureInfoVo pictureInfoVo = new UserPictureInfoVo();
        BeanUtils.copyProperties(pictureInfo, pictureInfoVo);
        return pictureInfoVo;
    }

    /**
     * 对象转换集合
     *
     * @param pictureInfoList
     * @return
     */
    public static List<UserPictureInfoVo> objToVo(List<PictureInfo> pictureInfoList) {
        if (pictureInfoList == null) {
            return null;
        }
        return pictureInfoList.stream().map(UserPictureInfoVo::objToVo).toList();
    }
}
