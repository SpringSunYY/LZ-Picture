package com.lz.picture.model.dto.pictureInfo;

import com.lz.picture.model.domain.PictureInfo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 图片信息Vo对象 p_picture_info
 * 用户更新图片
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class UserPictureInfoUpdate implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片编号
     */
    private String pictureId;

    /**
     * 图片名称
     */
    @NotEmpty(message = "图片名称不能为空")
    private String name;

    /**
     * 简介
     */
    @Size(max = 512, message = "简介不能超过512个字符")
    private String introduction;

    /**
     * 分类编号
     */
    private String categoryId;


    /**
     * 所属空间编号
     */
    @NotEmpty(message = "所属空间编号不能为空")
    private String spaceId;

    /**
     * 所属文件夹编号
     */
    private String folderId;

    /**
     * 图片标签
     */
    private List<String> tags;

    /**
     * 对象转封装类
     *
     * @param pictureInfoInsert 插入对象
     * @return PictureInfoInsert
     */
    public static PictureInfo updateToObj(UserPictureInfoUpdate pictureInfoInsert) {
        if (pictureInfoInsert == null) {
            return null;
        }
        PictureInfo pictureInfo = new PictureInfo();
        BeanUtils.copyProperties(pictureInfoInsert, pictureInfo);
        return pictureInfo;
    }
}
