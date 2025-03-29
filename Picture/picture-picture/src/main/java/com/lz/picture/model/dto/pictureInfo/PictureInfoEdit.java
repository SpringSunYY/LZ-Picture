package com.lz.picture.model.dto.pictureInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
/**
 * 图片信息Vo对象 p_picture_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class PictureInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 图片编号 */
    private String pictureId;

    /** 图片URL */
    private String pictureUrl;

    /** 图片名称 */
    private String name;

    /** 简介 */
    private String introduction;

    /** 分类编号 */
    private String categoryId;

    /** 图片体积（字节） */
    private Long picSize;

    /** 图片宽度 */
    private Long picWidth;

    /** 图片高度 */
    private Long picHeight;

    /** 宽高比例 */
    private Double picScale;

    /** 图片格式 */
    private String picFormat;

    /** 所需积分 */
    private Long pointsNeed;

    /** 上传用户编号 */
    private String userId;

    /** 图片状态（0公共 1私有） */
    private String pictureStatus;

    /** 审核状态（0待审核 1通过 2拒绝） */
    private Long reviewStatus;

    /** 审核信息 */
    private String reviewMessage;

    /** 审核人编号 */
    private Long reviewUserId;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reviewTime;

    /** 缩略图URL */
    private String thumbnailUrl;

    /** 所属空间编号 */
    private String spaceId;

    /** 所属文件夹编号 */
    private String folderId;

    /** 图片主色调（十六进制代码） */
    private String picColor;

    /** 删除（0否 1是） */
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
