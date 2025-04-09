package com.lz.picture.model.dto.pictureInfo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.PictureInfo;

/**
 * 图片信息Vo对象 p_picture_info
 *
 * @author YY
 * @date 2025-04-09
 */
@Data
public class PictureInfoEdit implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 图片编号
     */
    private String pictureId;

    /**
     * 图片URL
     */
    private String pictureUrl;

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
     * 图片格式
     */
    private String picFormat;

    /**
     * 所需积分
     */
    private Long pointsNeed;

    /**
     * 上传用户编号
     */
    private String userId;

    /**
     * 图片状态（0公共 1私有）
     */
    private String pictureStatus;

    /**
     * 审核状态（0待审核 1通过 2拒绝）
     */
    private Long reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;


    /**
     * 更多信息
     */
    private String moreInfo;

    /**
     * 删除（0否 1是）
     */
    private String isDelete;

    /**
     * 对象转封装类
     *
     * @param pictureInfoEdit 编辑对象
     * @return PictureInfo
     */
    public static PictureInfo editToObj(PictureInfoEdit pictureInfoEdit) {
        if (pictureInfoEdit == null) {
            return null;
        }
        PictureInfo pictureInfo = new PictureInfo();
        BeanUtils.copyProperties(pictureInfoEdit, pictureInfo);
        return pictureInfo;
    }
}
