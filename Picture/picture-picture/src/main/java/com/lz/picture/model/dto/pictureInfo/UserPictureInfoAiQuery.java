package com.lz.picture.model.dto.pictureInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 图片信息Query对象 p_picture_info
 * 用户查询
 *
 * @author YY
 * date 2025-04-09
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class UserPictureInfoAiQuery implements Serializable {
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
     * 上传用户编号
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    private String pictureStatus;

    /**
     * 当前记录起始索引
     */
    private Integer pageNum;

    /**
     * 每页显示记录数
     */
    private Integer pageSize;


    /**
     * 对象转封装类
     *
     * @param pictureInfoQuery 查询对象
     * @return PictureInfo
     */
    public static PictureInfo queryToObj(UserPictureInfoAiQuery pictureInfoQuery) {
        if (pictureInfoQuery == null) {
            return null;
        }
        PictureInfo pictureInfo = new PictureInfo();
        BeanUtils.copyProperties(pictureInfoQuery, pictureInfo);
        return pictureInfo;
    }
}
