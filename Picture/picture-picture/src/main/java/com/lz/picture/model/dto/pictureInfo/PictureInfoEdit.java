package com.lz.picture.model.dto.pictureInfo;

import com.lz.common.annotation.Excel;
import com.lz.picture.model.domain.PictureInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 图片信息Vo对象 p_picture_info
 *
 * @author YY
 * @date 2025-04-09
 */
@Data
public class PictureInfoEdit implements Serializable {
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
     * 图片格式
     */
    private String picFormat;

    /**
     * 图片状态（0公共 1私有）
     */
    private String pictureStatus;

    /**
     * 查看次数
     */
    @Excel(name = "查看次数")
    private Long lookCount;

    /**
     * 收藏次数
     */
    private Long collectCount;

    /**
     * 点赞次数
     */
    private Long likeCount;

    /**
     * 分享次数
     */
    private Long shareCount;


    /**
     * 下载次数
     */
    private Long downloadCount;

    /**
     * 上传类型
     */
    private String uploadType;


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
