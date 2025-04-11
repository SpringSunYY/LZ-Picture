package com.lz.picture.model.vo.pictureInfo;

import com.lz.picture.model.domain.PictureInfo;
import com.lz.user.model.vo.userInfo.UserVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 图片信息Vo对象 p_picture_info
 *
 * @author YY
 * @date 2025-04-09
 */
@Data
public class UserPictureDetailInfoVo implements Serializable {
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
    private String categoryId;
    private String categoryName;


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
     * 上传用户编号
     */
    private String userId;
    private String userName;
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
     * 所属文件夹编号
     */
    private String folderId;

    /**
     * 图片标签
     */
    private List<String> pictureTags;


    /**
     * 对象转封装类
     *
     * @param pictureInfo PictureInfo实体对象
     * @return PictureInfoVo
     */
    public static UserPictureDetailInfoVo objToVo(PictureInfo pictureInfo) {
        if (pictureInfo == null) {
            return null;
        }
        UserPictureDetailInfoVo pictureInfoVo = new UserPictureDetailInfoVo();
        BeanUtils.copyProperties(pictureInfo, pictureInfoVo);
        return pictureInfoVo;
    }

    /**
     * 对象转换集合
     *
     * @param pictureInfoList
     * @return
     */
    public static List<UserPictureDetailInfoVo> objToVo(List<PictureInfo> pictureInfoList) {
        if (pictureInfoList == null) {
            return null;
        }
        return pictureInfoList.stream().map(UserPictureDetailInfoVo::objToVo).toList();
    }
}
