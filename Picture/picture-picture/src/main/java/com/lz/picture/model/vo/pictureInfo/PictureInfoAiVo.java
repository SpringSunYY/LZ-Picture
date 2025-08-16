package com.lz.picture.model.vo.pictureInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图片信息Vo对象 p_picture_info
 *
 * @author YY
 * @date 2025-04-09
 */
@Data
public class PictureInfoAiVo implements Serializable {
    @Serial
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
     * 图片宽度
     */
    private Integer picWidth;

    /**
     * 图片高度
     */
    private Integer picHeight;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;


    /**
     * 查看次数
     */
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
     * 对象转封装类
     *
     * @param pictureInfo PictureInfo实体对象
     * @return PictureInfoVo
     */
    public static PictureInfoAiVo objToVo(PictureInfo pictureInfo) {
        if (pictureInfo == null) {
            return null;
        }
        PictureInfoAiVo pictureInfoVo = new PictureInfoAiVo();
        BeanUtils.copyProperties(pictureInfo, pictureInfoVo);
        return pictureInfoVo;
    }

    /**
     * 对象装封装类
     *
     * @param pictureInfos 实体对象
     * @return PictureInfo
     */
    public static List<PictureInfoAiVo> objToVo(List<PictureInfo> pictureInfos) {
        if (pictureInfos == null) {
            return null;
        }
        return pictureInfos.stream().map(PictureInfoAiVo::objToVo).collect(Collectors.toList());
    }
}
