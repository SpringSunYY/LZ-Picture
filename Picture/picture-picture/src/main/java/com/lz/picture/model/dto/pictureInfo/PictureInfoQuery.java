package com.lz.picture.model.dto.pictureInfo;

import java.util.Map;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureInfo;

/**
 * 图片信息Query对象 p_picture_info
 *
 * @author YY
 * @date 2025-04-09
 */
@Data
public class PictureInfoQuery implements Serializable {
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
     * 域名URL
     */
    private String dnsUrl;

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
     * 宽高比例
     */
    private Double picScale;

    /**
     * 图片格式
     */
    private String picFormat;

    /**
     * 所需积分
     */
    private Long pointsNeed;

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
     * 编辑时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date editTime;

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
     * 审核状态（0待审核 1通过 2拒绝）
     */
    private String reviewStatus;

    /**
     * 审核人编号
     */
    private Long reviewUserId;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reviewTime;

    /**
     * 所属空间编号
     */
    private String spaceId;

    /**
     * 所属文件夹编号
     */
    private String folderId;

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
