package com.lz.picture.model.dto.pictureInfo;

import com.lz.picture.model.domain.PictureInfo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 图片信息Vo对象 p_picture_info
 * 用户更新图片名称
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class UserPictureInfoUpdateName implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片编号
     */
    @NotEmpty(message = "图片编号不能为空")
    private String pictureId;

    /**
     * 图片名称
     */
    @NotEmpty(message = "图片名称不能为空")
    @Length(max = 32, message = "图片名称长度不能超过32个字符")
    private String name;


    /**
     * 对象转封装类
     *
     * @param pictureInfoInsert 插入对象
     * @return PictureInfoInsert
     */
    public static PictureInfo updateToObj(UserPictureInfoUpdateName pictureInfoInsert) {
        if (pictureInfoInsert == null) {
            return null;
        }
        PictureInfo pictureInfo = new PictureInfo();
        BeanUtils.copyProperties(pictureInfoInsert, pictureInfo);
        return pictureInfo;
    }
}
