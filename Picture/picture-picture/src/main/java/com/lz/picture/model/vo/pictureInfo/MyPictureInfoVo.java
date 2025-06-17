package com.lz.picture.model.vo.pictureInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 图片信息Vo对象 p_picture_info
 * 用户自己展示的列表
 *
 * @author YY
 * @date 2025-04-09
 */
@Data
public class MyPictureInfoVo implements Serializable {
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
     * 简介
     */
    private String introduction;

    /**
     * 分类编号
     */
    private String categoryId;

    /**
     * 图片体积
     */
    private Long picSize;

    /**
     * 图片宽度
     */
    private Long picWidth;

    /**
     * 图片高度
     */
    private Long picHeight;

    /**
     * 宽高比例
     */
    private Double picScale;

    /**
     * 图片格式
     */
    private String picFormat;

    /**
     * 上传用户编号
     */
    private String userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 图片状态（0公共 1私有）
     */
    private String pictureStatus;

    /**
     * 审核状态（0待审核 1通过 2拒绝）
     */
    private String reviewStatus;

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;

    /**
     * 对象转封装类
     *
     * @param pictureInfo PictureInfo实体对象
     * @return PictureInfoVo
     */
    public static MyPictureInfoVo objToVo(PictureInfo pictureInfo) {
        if (pictureInfo == null) {
            return null;
        }
        MyPictureInfoVo pictureInfoVo = new MyPictureInfoVo();
        BeanUtils.copyProperties(pictureInfo, pictureInfoVo);
        return pictureInfoVo;
    }

    public static List<MyPictureInfoVo> objToVo(List<PictureInfo> pictureInfoList) {
        if (pictureInfoList == null) {
            return null;
        }
        return pictureInfoList.stream().map(MyPictureInfoVo::objToVo).toList();
    }
}
