package com.lz.picture.model.vo.pictureTagRelInfo;

import com.lz.common.annotation.Excel;
import com.lz.picture.model.domain.PictureTagRelInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 图片标签关联Vo对象 p_picture_tag_rel_info
 *
 * @author YY
 * @date 2025-06-04
 */
@Data
public class PictureTagRelInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关联编号
     */
    @Excel(name = "关联编号")
    private String relId;

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
     * 标签编号
     */
    @Excel(name = "标签编号")
    private String tagId;

    /**
     * 标签名称
     */
    @Excel(name = "标签名称")
    private String tagName;

    /**
     * 查看次数
     */
    @Excel(name = "查看次数")
    private Long lookCount;

    /**
     * 收藏次数
     */
    @Excel(name = "收藏次数")
    private Long collectCount;

    /**
     * 点赞次数
     */
    @Excel(name = "点赞次数")
    private Long likeCount;

    /**
     * 分享次数
     */
    @Excel(name = "分享次数")
    private Long shareCount;

    /**
     * 下载次数
     */
    @Excel(name = "下载次数")
    private Long downloadCount;

    /**
     * 所属用户
     */
    @Excel(name = "所属用户")
    private String userId;


    /**
     * 对象转封装类
     *
     * @param pictureTagRelInfo PictureTagRelInfo实体对象
     * @return PictureTagRelInfoVo
     */
    public static PictureTagRelInfoVo objToVo(PictureTagRelInfo pictureTagRelInfo) {
        if (pictureTagRelInfo == null) {
            return null;
        }
        PictureTagRelInfoVo pictureTagRelInfoVo = new PictureTagRelInfoVo();
        BeanUtils.copyProperties(pictureTagRelInfo, pictureTagRelInfoVo);
        return pictureTagRelInfoVo;
    }
}
