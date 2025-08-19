package com.lz.picture.model.vo.pictureInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.dto.pictureInfo.PictureMoreInfo;
import com.lz.user.model.vo.userInfo.UserVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 图片信息Vo对象 p_picture_info
 * 用户ai详情
 * @author YY
 * @date 2025-08-19
 */
@Data
public class PictureDetailInfoAiVo implements Serializable {
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
    private String categoryName;


    /**
     * 图片宽度
     */
    private Integer picWidth;

    /**
     * 图片高度
     */
    private Integer picHeight;

    /**
     * 用户信息
     */
    private UserVo userInfoVo;


    /**
     * 缩略图URL
     */
    private String thumbnailUrl;

    /**
     * 所属空间编号
     */
    private String spaceId;
    private String spaceName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /**
     * 更多信息
     */
    private PictureMoreInfo moreInfo;


    /**
     * 图片标签
     */
    private List<String> pictureTags;

    /**
     * 查看次数
     */
    private Long lookCount;

    /**
     * 点赞数
     */
    private Long likeCount;

    /**
     * 是否点赞
     */
    private Boolean isLike;

    /**
     * 收藏数
     */
    private Long collectCount;

    /**
     * 是否收藏
     */
    private Boolean isCollect;

    /**
     * 分享数
     */
    private Long shareCount;


    /**
     * 对象转封装类
     *
     * @param pictureInfo PictureInfo实体对象
     * @return PictureInfoVo
     */
    public static PictureDetailInfoAiVo objToVo(PictureInfo pictureInfo) {
        if (pictureInfo == null) {
            return null;
        }
        PictureDetailInfoAiVo pictureInfoVo = new PictureDetailInfoAiVo();
        BeanUtils.copyProperties(pictureInfo, pictureInfoVo);
        return pictureInfoVo;
    }

    /**
     * 对象转换集合
     *
     * @param pictureInfoList
     * @return
     */
    public static List<PictureDetailInfoAiVo> objToVo(List<PictureInfo> pictureInfoList) {
        if (pictureInfoList == null) {
            return null;
        }
        return pictureInfoList.stream().map(PictureDetailInfoAiVo::objToVo).toList();
    }
}
