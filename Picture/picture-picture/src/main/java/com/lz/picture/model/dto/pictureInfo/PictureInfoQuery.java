package com.lz.picture.model.dto.pictureInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.picture.model.domain.PictureInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 图片信息Query对象 p_picture_info
 *
 * @author YY
 * @date 2025-04-09
 */
@Data
public class PictureInfoQuery implements Serializable {
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

    /**
     * 图片体积
     */
    private Long picSize;

    /**
     * 图片宽度
     */
    private Integer picWidth;

    /**
     * 图片高度
     */
    private Integer picHeight;

    /**
     * 宽高比例
     */
    private Double picScale;

    /**
     * 图片格式
     */
    private String picFormat;

    /**
     * 上传用户编号
     */
    private String userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date publishTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /**
     * 图片状态（0公共 1私有）
     */
    private String pictureStatus;

    /**
     * 所属空间编号
     */
    private String spaceId;

    /**
     * 所属文件夹编号
     */
    private String folderId;

    /**
     * 上传类型
     */
    private String uploadType;

    /**
     * 删除（0否 1是）
     */
    private String isDelete;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deletedTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param pictureInfoQuery 查询对象
     * @return PictureInfo
     */
    public static PictureInfo queryToObj(PictureInfoQuery pictureInfoQuery) {
        if (pictureInfoQuery == null) {
            return null;
        }
        PictureInfo pictureInfo = new PictureInfo();
        BeanUtils.copyProperties(pictureInfoQuery, pictureInfo);
        return pictureInfo;
    }
}
